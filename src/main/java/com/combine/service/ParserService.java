package com.combine.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.combine.dal.DataSourceLayer;
import com.combine.model.College;
import com.combine.model.Conference;
import com.combine.model.Participant;
import com.combine.model.Player;
import com.combine.model.Workout;
import com.combine.model.WorkoutResult;

public class ParserService {

	private static final int DRAFT_LISTENER_WAIT_TIME = 120000; //2 minutes in ms
	private static final Logger logger = Logger.getLogger(ParserService.class);
	private static final String JSON_URL = "http://www.nfl.com/liveupdate/combine/2017/";
	private static final String PROFILES_URL = "http://www.nfl.com/combine/profiles/";
	private static final String ASP_EXT = ".asp";
	private static final String DRAFT_TEK = "http://www.drafttek.com/Top-100-NFL-Draft-Prospects-2017";
	private static final String DRAFT_TEK_PAGE = "http://www.drafttek.com/Top-100-NFL-Draft-Prospects-2017-Page-";
	
	private static final String CBS_SPORTS_DRAFT = "http://www.cbssports.com/nfl/draft/prospect-rankings-results/${year}/${pos}/overall?&print_rows=9999";
	private static final List<String> ALL_POSITIONS = Arrays.asList("C","CB","DE", "DT", "FB", "FS", "ILB", "K", "LS", "OG", "OLB", "OT", "P", "QB", "RB", "SS", "TE", "WR");
	private RestTemplate restTemplate;
	private DataSourceLayer dataSourceLayer;
	private ConversionService conversionService;
	private JSONService jsonService;
	
	Set<String> missingSchools = new HashSet<>();
	
	private List<Player> players = new ArrayList<>();;
	
	public ParserService(){
		this.jsonService = new JSONService();
	}

	public ParserService(DataSourceLayer dataSourceLayer) {
		this.restTemplate = new RestTemplate();
		this.conversionService = new ConversionService(dataSourceLayer);
		this.dataSourceLayer = dataSourceLayer;
		this.jsonService = new JSONService();
	}

	public void parse() {
		this.dataSourceLayer.clearDb();
//		List<Participant> participants = this.retrieveParticipants();
		List<Participant> participants = new ArrayList<>();
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
	
	/**
	 * get the picks / prospects json objects from nfl.com script
	 * @return
	 */
	public Map<String, JSONObject> loadDraftPicksJs(){
		Map<String, JSONObject> map = new HashMap<>();
		try {
			
			//load the doc
			Document doc = Jsoup.connect("http://www.nfl.com/draft/2017/tracker?icampaign=draft-sub_nav_bar-drafteventpage-tracker").timeout(3000).get();
			
			boolean found = false;
			boolean hasNext = true;
			int startIdx = 0;
			JSONObject prospects = new JSONObject();
			JSONObject picks = new JSONObject();
			while(!found && hasNext){
				
				//find the text nfl.global.dt.data
				int index = doc.toString().indexOf("nfl.global.dt.data", startIdx);
				
				//if not found (hopefully this doesn't happen), then break out
				if(index < 0){
					hasNext = false;
				} else{
					
					//get the text from the variable name to the end of the statement (terminated by a semi-colon)
					String json = doc.toString().substring(index, doc.toString().indexOf(";", index));
					
					//get the start index by finding the first opene bracket after the variable name
					int start = json.indexOf("{") - 1;
					JSONObject obj = new JSONObject(json.substring(start));
					
					try{
						
						//parse the two objects & break out of the loop
						prospects = obj.getJSONObject("prospects");
						picks = obj.getJSONObject("picks");
						found = true;
					} catch(JSONException e){
						startIdx = index + 1;
					}
				}	
			}
			
			System.out.println("found " + prospects.keySet().size() + " prospects");
			System.out.println("found " + picks.keySet().size() + " picks");
			map.put("prospects", prospects);
			map.put("picks", picks);
			return map;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Retrieves draft info from nfl.com script file, and updates player table in db with the draft information (pick / round / team).
	 * Method waits two minutes before getting an updated version of the script data.
	 * Just a convenience method so I don't need to continually run this manually.
	 * 
	 */
	public void updateDraftPicks() {
		boolean running = true;
		while(running){
			
			//load the draft "picks" json object
			Map<String, JSONObject> map = loadDraftPicksJs(); 
			JSONObject picks = map.get("picks");
			JSONObject prospects = map.get("prospects");
			
			//if not null
			if(picks != null){
				
				//save the keyset into a list for sorting (allows for prompt exiting... once first empty pick reached, can stop execution)
				List<Object> listObj = new ArrayList<>(picks.keySet());
				Collections.sort(listObj, (a,b)->{
					Integer num1 = Integer.parseInt(a.toString());
					Integer num2 = Integer.parseInt(b.toString());
					return num1 > num2 ? 1 : num1 < num2 ? -1 : 0;
				});
				
				//loop over each key (1 through 253 [total number of picks])
				for (Object key : listObj) {
					
					//get the object
					JSONObject pick = picks.getJSONObject(key.toString());
	
					try {
						
						//try to load the player info for the pick.  this will throw an error if the pick hasn't been made yet, and the app will wait 2 minutes,
						//before loading the js file & trying again
						String playerId = pick.getString("player");
						if(StringUtils.isEmpty(playerId)){
							//note that player should be null (which will result in the catch block being reached.
							//but if it's an empty string, this extra check is here
							throw new JSONException("Reached current pick: " + key.toString());
						}
						
						//if a player was found, then find the player details via the playerid on the pick object
						JSONObject player = this.jsonService.findByKey(prospects, playerId);
						if(player != null){
							
							//get the player name, and also set the draft pick info
							String firstname = this.jsonService.getStringFromJSON(player, "firstName");
							String lastname = this.jsonService.getStringFromJSON(player, "lastName");
							Integer roundNumber = pick.getInt("round");
							Integer pickNumber = pick.getInt("pick");
							String team = pick.getString("team");
		
							//lookup the player in the app's draft db
							Player p = this.conversionService.findPlayerByNflData(firstname, lastname, "", "", "");
							if(p != null && (p.getRound() == null || p.getRound() == 0)){
								
								//update the draft fields for the player
								p.setRound(roundNumber);
								p.setPick(pickNumber);
								p.setTeam(team);
								if(this.dataSourceLayer.getCombineDao().updatePick(p) < 1){
									System.out.println("Unable to find " + pick.toString());
								}
							}
						}
						
					} catch (NullPointerException | JSONException e) {
						
						//on an error, break out of the loop
						logger.warn("Reached current pick: " + key.toString());
						break;
					}
				}			
			}
			
			try {
				Thread.sleep(DRAFT_LISTENER_WAIT_TIME);
			} catch (InterruptedException e) {
				running = false;
				e.printStackTrace();
			}
		}

	}

	public void retrieveParticipants() {

		String response = this.jsonService.loadJson("2017_prospects.json");
		List<String> errors = new ArrayList<>();
		JSONObject obj = new JSONObject(response);
		
		int count = 1;
		int total = 0;
		for (Object key : obj.keySet()) {
			System.out.println("parsing participant " + (count++) + " of " + obj.keySet().size());

			JSONObject prospect = obj.getJSONObject(key.toString());
			boolean inserted = false;
			try {
				
				String firstname = this.jsonService.getStringFromJSON(prospect, "firstName");
				String lastname = this.jsonService.getStringFromJSON(prospect, "lastName");

				Player p = this.conversionService.findPlayerByNflData(firstname, lastname, "", "", "");
				if(p != null){
					double armLength = this.conversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "armLength"));
					double handSize = this.conversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "handSize"));
					if(armLength > 0 || handSize > 0){
						p.setHandSize(handSize);
						p.setArmLength(armLength);
						inserted = this.dataSourceLayer.getCombineDao().updateArmLengthAndHandSize(p) > 0;
					}
				}
			
//				p.setPosition(Position.stringToId(this.jsonService.getStringFromJSON(prospect, "pos"),
//						this.dataSourceLayer.getCombineDao().getPositions()));
//				p.setCollege(this.jsonService.getIntFromJSON(prospect, "collegeid"));
//				p.setWeight(this.jsonService.getIntFromJSON(prospect, "weight"));
//				p.setHeight(this.conversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "height")));
				
//				p.setLink(this.jsonService.getStringFromJSON(prospect, "linkName"));
//				p.setExpertGrade(this.jsonService.getDoubleFromJSON(prospect, "expertGrade"));
//				p.setPick(this.jsonService.getIntFromJSON(prospect, "pick"));
//				this.parsePlayerInfo(p);
//				participants.add(p);
				
			} catch (Exception e) {
				logger.warn("Error adding participant: " + prospect.toString());
//				participants.add(p);
			}
			if(!inserted){
				errors.add("unable to find match for or no data existed for: " + prospect.toString());
			} else{
				total++;
			}
		}
		System.out.println("Found data for " + total + " players.");
//		return participants;
	}
	
	//given a string, variable name (the field to be interpolated), and a value, perform some dirty interpolation
	private String interpolate(String string, String target, String value){
		String interpolated = string.replaceAll(Pattern.quote("${"+ target +"}"), value);
		return interpolated;
	}
	
	public void loadCbsSportsDraft() throws IOException{
		
		String importUUID = UUID.randomUUID().toString();
		
		for(int year = 2017; year <= 2020; year++){
			
			String url = interpolate(CBS_SPORTS_DRAFT, "year", String.valueOf(year));
			for(String positionCategory : ALL_POSITIONS){
				Document doc = Jsoup.connect(interpolate(url.toString(), "pos", positionCategory)).timeout(20*1000).get();
				Element table = doc.getElementById("prospectRankingsTable");
				List<Element> rows = table.getElementsByTag("tr");
				
				//for the first row, map headers
				Map<Integer,String> headers = new HashMap<>();
				for(int i = 0; i < rows.get(0).getElementsByTag("td").size(); i++){
					String header = rows.get(0).getElementsByTag("td").get(i).getElementsByTag("a").html();
					headers.put(i, header);
				}
	
				//for all other rows, create player from data and add to array list
				for(int i = 1; i < rows.size(); i++){
					Element row = rows.get(i);
					List<Element> tdElements = row.getElementsByTag("td");
					Player player = new Player();
					for(int j = 0; j < tdElements.size(); j++){
						
						//get value & null check
						String value = tdElements.get(j).html();
						if(!StringUtils.isEmpty(value)){
							
							//determine what field on the player object should be set to the current value
							String currentHeader = headers.get(j);
							if("Rank".equals(headers.get(j))){
								try{
									player.setRank(Integer.parseInt(value));
								} catch(NumberFormatException ex){
									player.setRank(0);
								}
							}
							else if("Player".equals(currentHeader)){
								if(value.contains("href=")){
									player.setName(tdElements.get(j).getElementsByTag("a").get(0).html());
								} else if(value.contains("<img")){
									player.setName(value.substring(0,value.indexOf("<")));
								} else{
									player.setName(value);
								}
							}
							else if("Pos. Rank".equals(currentHeader)){
								try{
									player.setPositionRank(Integer.parseInt(value));
								} catch(NumberFormatException ex){
									player.setPositionRank(0);
								}
							}
							else if("School".equals(currentHeader)){
								player.setCollege(this.conversionService.collegeNameToId(value.replace("amp;", "")));
								player.setCollegeText(value.replace("amp;", ""));
							}
							else if("Class".equals(currentHeader)){
								player.setYearClass(value);
							}
							else if("Ht.".equals(currentHeader)){
								player.setHeight(this.conversionService.toRawInches(value));
							}
							else if("Wt.".equals(currentHeader)){
								player.setWeight(Double.parseDouble(value));
							}
							else if("Proj. Round".equals(currentHeader)){
								if(value.contains("&#x")){
									player.setProjectedRound("Undrafted");
								} else{
									player.setProjectedRound(value);
								}
							}
						}
					}
					
					//if not a bogus record, add to list
					if(!StringUtils.isEmpty(player.getName()) && player.getName().length() > 2){
						player.setYear(year);
						player.setPosition(positionCategory);
						player.setImportUUID(importUUID);
						this.players.add(player);
					}
					
				}
			}
			
//			this.dataSourceLayer.clearPlayersByYear(year);
			int count = this.dataSourceLayer.addPlayers(players);
			System.out.println(count + " records retrieved for " + year);	
		}
	}
	
	public void loadCombineDataForCbsSportsDraft() {

		ArrayList<String> errors = new ArrayList<>();
		String response = this.jsonService.loadJson("nfl_draft_2017_data.json");

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
			boolean inserted = false;
			try {
				if(i == 191){
					System.out.println("pause");
				}
				String firstname = prospect.getString("firstName").replaceAll("[^a-zA-Z]", "");
				String lastname = prospect.getString("lastName").replaceAll("[^a-zA-Z]", "");
				String college = prospect.getString("college");
				String position = prospect.getString("position");
				String conference = prospect.getString("conference");
				
				Player p = this.conversionService.findPlayerByNflData(firstname, lastname, college, conference, position);
				if(p != null){
					p.setFortyYardDash(this.jsonService.getDoubleFromJSON(prospect, "fortyYardDash"));
					p.setBenchPress(this.jsonService.getDoubleFromJSON(prospect, "benchPress"));
					p.setVerticalJump(this.jsonService.getDoubleFromJSON(prospect, "verticalJump"));
					p.setBroadJump(this.jsonService.getDoubleFromJSON(prospect, "broadJump"));
					p.setThreeConeDrill(this.jsonService.getDoubleFromJSON(prospect, "threeConeDrill"));
					p.setTwentyYardShuttle(this.jsonService.getDoubleFromJSON(prospect, "twentyYardShuttle"));
					p.setSixtyYardShuttle(this.jsonService.getDoubleFromJSON(prospect, "sixtyYardShuttle"));
					p.setArmLength(this.conversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "armLength")));
					p.setHandSize(this.conversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "handSize")));
					
					inserted = this.dataSourceLayer.getCombineDao().updateWorkoutResults(p) > 0;
				}

			} catch (Exception e) {
				logger.warn("Error adding participant: " + prospect.toString());
			}
			
			if(!inserted){
				errors.add("unable to find match for: " + prospect.toString());
			}
		}
		
		System.out.println("pause...");
		
	}

	public void loadDraftTek() throws IOException{
		
		for (int i = 1; i <= 3; i++) {
			String url = (i > 1 ? DRAFT_TEK_PAGE + i : DRAFT_TEK) + ASP_EXT;
			Document doc = Jsoup.connect(url).get();
			Element wrapper = doc.getElementById("content");
			List<Element> rows = wrapper.getElementsByClass("BigBoardColor1");
			for(Element row : rows){
				List<Element> tdElements = row.getElementsByTag("td");
				Player player = new Player();
				for(int j = 0; j < tdElements.size(); j++){
					String value = tdElements.get(j).html();
					if(!StringUtils.isEmpty(value)){
						switch (j) {
						case 0:
							player.setRank(Integer.parseInt(value));
							break;
						case 2:
							player.setName(value);
							break;
						case 3:
							player.setCollege(this.conversionService.collegeNameToId(value));
							break;
						case 4:
							player.setPosition(value);
							break;
						case 5:
							player.setHeight(this.conversionService.toRawInches(value));
							break;
						case 6:
							player.setWeight(Double.parseDouble(value));
							break;
						default:
							break;
						}
					}
				}
				
				if(this.players.stream().filter(p->p.getName() == null || player.getName() == null || p.getName().toLowerCase().equals(player.getName().toLowerCase())).collect(Collectors.toList()).isEmpty()){
					players.add(player);
				}
			}
		}
		System.out.println(players.size());
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
