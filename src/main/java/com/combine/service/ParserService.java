package com.combine.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.client.RestTemplate;

import com.combine.dal.DataSourceLayer;
import com.combine.model.College;
import com.combine.model.Conference;
import com.combine.model.Participant;
import com.combine.model.Position;
import com.combine.model.Workout;
import com.combine.model.WorkoutResult;

public class ParserService {

	private static final Logger logger = Logger.getLogger(ParserService.class);
	private static final String JSON_URL = "http://www.nfl.com/liveupdate/combine/2016/";
	private static final String PROFILES_URL = "http://www.nfl.com/combine/profiles/";
	private RestTemplate restTemplate;
	private DataSourceLayer dataSourceLayer;
	private ConversionService conversionService;
	private JSONService jsonService;

	public ParserService(DataSourceLayer dataSourceLayer) {
		this.restTemplate = new RestTemplate();
		this.conversionService = new ConversionService();
		this.dataSourceLayer = dataSourceLayer;
		this.jsonService = new JSONService();
	}

	public void parse() {
		this.dataSourceLayer.clearDb();
		List<Participant> participants = this.retrieveParticipants();
		List<WorkoutResult> workoutResults = this.getWorkoutResults();
		this.dataSourceLayer.addParticipants(participants);
		this.dataSourceLayer.addWorkoutResults(workoutResults);
	}

	public void insertColleges() {
		String conferenceResponse = this.jsonService.loadJson("conferences.json");
		List<Conference> conferences = new ArrayList<>();
		JSONArray confArray = new JSONArray(conferenceResponse);
		for (int i = 0; i < confArray.length(); i++) {
			JSONObject obj = confArray.getJSONObject(i);
			Conference conf = new Conference();
			conf.setId(obj.getInt("id"));
			conf.setName(obj.getString("conf"));
			conferences.add(conf);
		}

		String collegeResponse = this.jsonService.loadJson("colleges.json");
		List<College> colleges = new ArrayList<>();
		JSONArray collegesArray = new JSONArray(collegeResponse);
		for (int i = 0; i < collegesArray.length(); i++) {
			JSONObject obj = collegesArray.getJSONObject(i);
			College college = new College();
			college.setId(obj.getInt("id"));
			college.setConf(obj.getInt("conf"));
			college.setName(obj.getString("name"));
			colleges.add(college);
		}

		this.dataSourceLayer.addConferences(conferences);
		this.dataSourceLayer.addColleges(colleges);
	}

	private List<WorkoutResult> getWorkoutResults() {
		List<WorkoutResult> results = new ArrayList<WorkoutResult>();
		String response = null;
		JSONArray array = null;
		List<Workout> workouts = this.dataSourceLayer.getCombineDao().getWorkouts();
		for (int j = 0; j < workouts.size(); j++) {
			try {
				response = this.restTemplate.getForObject(JSON_URL + workouts.get(j).getName() + "/ALL.json",
						String.class);
				array = new JSONObject(response).getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = null;
					try {
						object = array.getJSONObject(i);
						WorkoutResult res = new WorkoutResult();
						res.setParticipant(object.getInt("id"));
						try {
							res.setResult(object.getDouble("result"));
						} catch (Exception e) {
							logger.warn("unable to load result for " + object.toString() + ". " + e.getMessage());
							res.setResult(null);
						}

						res.setWorkout(workouts.get(j).getId());
						results.add(res);
					} catch (Exception e) {
						logger.warn("error retrieving info for " + object.toString() + ". " + e.getMessage());
					}
				}

			} catch (Exception e) {
				logger.warn("error reading response for " + workouts.get(j).getName() + ". " + e.getMessage());
			}
		}
		return results;
	}

	public List<Participant> retrieveParticipants() {

		String response = this.jsonService.loadJson("nfl_draft_2016_data.json");

		List<Participant> participants = new ArrayList<>();
		JSONObject obj = new JSONObject(response);
		JSONArray prospects = null;

		try {
			prospects = obj.getJSONArray("prospects");
		} catch (Exception e) {
			System.out.println("error");
		}

		for (int i = 0; i < prospects.length(); i++) {
			System.out.println("parsing participant " + (i + 1) + " of " + prospects.length());
			JSONObject prospect = prospects.getJSONObject(i);
			Participant p = new Participant();
			try {
				p.setId(this.jsonService.getIntFromJSON(prospect, "personId"));
				p.setFirstname(this.jsonService.getStringFromJSON(prospect, "firstName"));
				p.setLastname(this.jsonService.getStringFromJSON(prospect, "lastName"));
				p.setPosition(Position.stringToId(this.jsonService.getStringFromJSON(prospect, "pos"),
						this.dataSourceLayer.getCombineDao().getPositions()));
				p.setCollege(this.jsonService.getIntFromJSON(prospect, "collegeid"));
				p.setWeight(this.jsonService.getIntFromJSON(prospect, "weight"));
				p.setHeight(this.conversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "height")));
				p.setHands(this.conversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "handSize")));
				p.setArmLength(this.conversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "armLength")));
				p.setLink(this.jsonService.getStringFromJSON(prospect, "linkName"));
				p.setExpertGrade(this.jsonService.getDoubleFromJSON(prospect, "expertGrade"));
				p.setPick(this.jsonService.getIntFromJSON(prospect, "pick"));
				this.parsePlayerInfo(p);
				participants.add(p);
			} catch (Exception e) {
				logger.warn("Error adding participant: " + prospect.toString());
				participants.add(p);
			}
		}
		return participants;
	}

	/**
	 * Overview == section where first h3 ==== Overview) p —> html Analysis ==
	 * section where first h3 is Analysys Strengths (article #1) Weaknesses
	 * (article #2) NFL Comparison (article #3) Bottom Line (article #4)
	 * 
	 * @throws IOException
	 */
	public void parsePlayerInfo(Participant participant) throws IOException {
		Elements playerInfoSections = null;
		try {
			Document doc = Jsoup.connect(PROFILES_URL + participant.getLink() + "?id=" + participant.getId()).get();

			// PLAYER_INFO
			Element playerInfo = doc.getElementById("player-info");
			playerInfoSections = playerInfo.select("section");

			// set overview text
			int sectionIndex = 0;
			Element overviewSection = playerInfoSections.get(sectionIndex);
			if (overviewSection.select("h3").html().equals("Overview")) {
				String overviewText = overviewSection.select("h3").get(0).siblingElements().get(0).html();
				participant.setOverview(overviewText);
				sectionIndex++;
			}

			// set additional analysis text
			Element analysysSection = playerInfoSections.get(sectionIndex);
			Elements analysysSub = analysysSection.select("article");
			for (int i = 0; i < analysysSub.size() - 1; i++) {
				Element mainElement = analysysSub.get(i);
				String field = mainElement.select("h4").html();
				String data = mainElement.html().replace(mainElement.select("h4").outerHtml(), "").trim();
				if ("strengths".equals(field.toLowerCase())) {
					participant.setStrengths(data);
				} else if ("weaknesses".equals(field.toLowerCase())) {
					participant.setWeaknesses(data);
				} else if ("sources tell us".equals(field.toLowerCase())
						|| "what scouts say".equals(field.toLowerCase())) {
					participant.setWhatScoutsSay(data);
				} else if ("bottom line".equals(field.toLowerCase())) {
					participant.setBottom_line(data);
				} else if ("nfl comparison".equals(field.toLowerCase())) {
					participant.setComparision(data);
				}
			}
		} catch (Exception e) {
			logger.warn("error loading player info for " + playerInfoSections.toString() + ". " + e.getMessage());
		}
	}
}
