package com.combine.service;

import com.combine.dao.CombineDao;
import com.combine.events.ParserProgressEvent;
import com.combine.events.ParserProgressEventPublisher;
import com.combine.model.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ParserService {

    private static final int DRAFT_LISTENER_WAIT_TIME = 120000; //2 minutes in ms
    private static final Logger logger = Logger.getLogger(ParserService.class);

    private static final String JSON_URL = "http://www.nfl.com/liveupdate/combine/2018/";
    private static final String PROFILES_URL = "http://www.nfl.com/combine/profiles/";
    private static final String ASP_EXT = ".asp";
    private static final String DRAFT_TEK = "http://www.drafttek.com/Top-100-NFL-Draft-Prospects-2018";
    private static final String DRAFT_TEK_PAGE = "http://www.drafttek.com/Top-100-NFL-Draft-Prospects-2018-Page-";

    private static final String CBS_SPORTS_DRAFT = "http://www.cbssports.com/nfl/draft/prospect-rankings-results/${year}/${pos}/overall?&print_rows=9999";
    private static final List<String> ALL_POSITIONS = Arrays.asList("C", "CB", "DE", "DT", "FB", "FS", "ILB", "K", "LS", "OG", "OLB", "OT", "P", "QB", "RB", "SS", "TE", "WR");
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private CombineDao combineDao;

    @Autowired
    private CombineImporterConversionService combineImporterConversionService;

    @Autowired
    private JSONService jsonService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ParserProgressEventPublisher parserProgressEventPublisher;

    public void parse() {
//        this.playerService.clearDb();
//		List<Participant> participants = this.retrieveParticipants();
//        List<Participant> participants = new ArrayList<>();
//        List<WorkoutResult> workoutResults = this.getWorkoutResults();
//        this.playerService.addParticipants(participants);
//        this.playerService.addWorkoutResults(workoutResults);
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

        List<College> colleges = loadColleges();

        this.playerService.addConferences(conferences);
        this.playerService.addColleges(colleges);
    }

    public List<College> loadColleges() {

        List<College> colleges = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("http://www.nfl.com/draft/2018/tracker?icampaign=draft-sub_nav_bar-drafteventpage-tracker").timeout(3000).get();
            int index = doc.toString().indexOf("nfl.global.dt.data.colleges");

            String collegeResponse = doc.toString().substring(index, doc.toString().indexOf(";", index));
            int start = collegeResponse.indexOf("{") - 1;
            JSONObject collegesJSON = new JSONObject(collegeResponse.substring(start));
            for (Object id : collegesJSON.keySet()) {
                JSONObject obj = collegesJSON.getJSONObject(id.toString());
                College college = new College();
                college.setId(obj.getInt("id"));
                college.setName(obj.getString("name"));
                colleges.add(college);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return colleges;
    }


    public void getCombineWorkoutResults(ParserProgressMessage progress) {

        List<College> colleges = this.loadColleges();
        String response = null;
        JSONArray array = null;
        List<Workout> workouts = this.combineDao.getWorkouts();
        int total = 0;

        //load workouts
        for (int j = 0; j < workouts.size(); j++) {

            progress.with((j + 1), workouts.size(), "Updating combine workouts [" + workouts.get(j).getName() + "]");
            this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress));

            try {
                response = this.restTemplate.getForObject(JSON_URL + workouts.get(j).getName() + "/ALL.json",
                        String.class);
                array = new JSONObject(response).getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = null;

                    try {
                        object = array.getJSONObject(i);
                        String name = (StringUtils.capitalize(object.getString("firstName").toLowerCase()) + " " + StringUtils.capitalize(object.getString("lastName").toLowerCase())).replaceAll("'", "");
                        String positionText = object.getString("position");
                        String collegeText = object.getString("college");
                        Integer college = -1;
                        College collegeObject = this.playerService.findCollegeByName(collegeText, colleges);
                        if (collegeObject != null) {
                            college = collegeObject.getId();
                        }

                        Player player = new Player();
                        player.setName(name);
                        player.setCollege(college);
                        player.setCollegeText(collegeText);
                        player.setPosition(positionText);
                        player.setYear(2018);

                        Double result = 0.0;
                        String workout = "";
                        try {

                            int count = 0;
                            result = object.getDouble("result");
                            workout = object.getString("workoutName");

                            switch (workout) {
                                case "Bench Press":
                                    count = this.combineDao.updateWorkoutResults(player, college, result, "bench_press");
                                    break;
                                case "40 Yard Dash":
                                    count = this.combineDao.updateWorkoutResults(player, college, result, "forty_yard_dash");
                                    break;
                                case "3 Cone Drill":
                                    count = this.combineDao.updateWorkoutResults(player, college, result, "three_cone_drill");
                                    break;
                                case "20 Yard Shuttle":
                                    count = this.combineDao.updateWorkoutResults(player, college, result, "twenty_yard_shuttle");
                                    break;
                                case "60 Yard Shuttle":
                                    count = this.combineDao.updateWorkoutResults(player, college, result, "sixty_yard_shuttle");
                                    break;
                                case "Vertical Jump":
                                    count = this.combineDao.updateWorkoutResults(player, college, result, "vertical_jump");
                                    break;
                                case "Broad Jump":
                                    count = this.combineDao.updateWorkoutResults(player, college, result, "broad_jump");
                                    break;
                            }
                            if (count == 0) {
                                logger.info("Pause");
                            }
                            total += count;

                        } catch (Exception e) {
                            logger.warn("unable to load result for " + object.toString() + ". " + e.getMessage());
                        }
                    } catch (Exception e) {
                        logger.warn("error retrieving info for " + object.toString() + ". " + e.getMessage());
                    }
                }

            } catch (Exception e) {
                logger.warn("error reading response for " + workouts.get(j).getName() + ". " + e.getMessage());
            }
        }

        logger.info("Updated " + total + " combine records");

        //load armLength / handSize combine results
        JSONObject nflComPlayerSource = loadDraftPicksJs().get("prospects");
        for (Object id : nflComPlayerSource.keySet()) {
            Player player = new Player();
            JSONObject nflComPlayer = nflComPlayerSource.getJSONObject(id.toString());
            String name = (StringUtils.capitalize(nflComPlayer.getString("firstName").toLowerCase()) + " " + StringUtils.capitalize(nflComPlayer.getString("lastName").toLowerCase())).replaceAll("'", "");
            Integer college = -1;
            try {
                college = nflComPlayer.getInt("college");
            } catch (JSONException e) {
                logger.warn("ERROR PARSING COLLEGE: " + e.toString());
            }
            player.setName(name);
            player.setCollege(college);
            player.setYear(2018);

            String handSize = nflComPlayer.getString("handSize");
            String armLength = nflComPlayer.getString("armLength");
            player.setHandSize(this.combineImporterConversionService.toRawInches(handSize));
            player.setArmLength(this.combineImporterConversionService.toRawInches(armLength));
            this.combineDao.updateArmHandsResults(player);
        }

    }

    /**
     * get the picks / prospects json objects from nfl.com script
     *
     * @return
     */
    public Map<String, JSONObject> loadDraftPicksJs() {
        Map<String, JSONObject> map = new HashMap<>();
        try {

            //load the doc
            Document doc = Jsoup.connect("http://www.nfl.com/draft/2018/tracker?icampaign=draft-sub_nav_bar-drafteventpage-tracker").timeout(3000).get();

            boolean found = false;
            boolean hasNext = true;
            int startIdx = 0;
            JSONObject prospects = new JSONObject();
            JSONObject picks = new JSONObject();
            while (!found && hasNext) {

                //find the text nfl.global.dt.data
                int index = doc.toString().indexOf("nfl.global.dt.data", startIdx);

                //if not found (hopefully this doesn't happen), then break out
                if (index < 0) {
                    hasNext = false;
                } else {

                    //get the text from the variable name to the end of the statement (terminated by a semi-colon)
                    String json = doc.toString().substring(index, doc.toString().indexOf(";", index));

                    //get the start index by finding the first opene bracket after the variable name
                    int start = json.indexOf("{") - 1;
                    JSONObject obj = new JSONObject(json.substring(start));

                    try {

                        //parse the two objects & break out of the loop
                        prospects = obj.getJSONObject("prospects");
                        picks = obj.getJSONObject("picks");
                        found = true;
                    } catch (JSONException e) {
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
     */
    public void updateDraftPicks() {
        boolean running = true;
        while (running) {

            //load the draft "picks" json object
            Map<String, JSONObject> map = loadDraftPicksJs();
            JSONObject picks = map.get("picks");
            JSONObject prospects = map.get("prospects");

            //if not null
            if (picks != null) {

                //save the keyset into a list for sorting (allows for prompt exiting... once first empty pick reached, can stop execution)
                List<Object> listObj = new ArrayList<>(picks.keySet());
                Collections.sort(listObj, (a, b) -> {
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
                        if (StringUtils.isEmpty(playerId)) {
                            //note that player should be null (which will result in the catch block being reached.
                            //but if it's an empty string, this extra check is here
                            throw new JSONException("Reached current pick: " + key.toString());
                        }

                        //if a player was found, then find the player details via the playerid on the pick object
                        JSONObject player = this.jsonService.findByKey(prospects, playerId);
                        if (player != null) {

                            //get the player name, and also set the draft pick info
                            String firstname = this.jsonService.getStringFromJSON(player, "firstName");
                            String lastname = this.jsonService.getStringFromJSON(player, "lastName");
                            Integer roundNumber = pick.getInt("round");
                            Integer pickNumber = pick.getInt("pick");
                            String team = pick.getString("team");

                            //lookup the player in the app's draft db
                            Player p = this.combineImporterConversionService.findPlayerByNflData(firstname, lastname, "", "", "");
                            if (p != null && (p.getRound() == null || p.getRound() == 0)) {

                                //update the draft fields for the player
                                p.setRound(roundNumber);
                                p.setPick(pickNumber);
                                p.setTeam(team);
                                if (this.combineDao.updatePick(p) < 1) {
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

                Player p = this.combineImporterConversionService.findPlayerByNflData(firstname, lastname, "", "", "");
                if (p != null) {
                    double armLength = this.combineImporterConversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "armLength"));
                    double handSize = this.combineImporterConversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "handSize"));
                    if (armLength > 0 || handSize > 0) {
                        p.setHandSize(handSize);
                        p.setArmLength(armLength);
                        inserted = this.combineDao.updateArmLengthAndHandSize(p) > 0;
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
            if (!inserted) {
                errors.add("unable to find match for or no data existed for: " + prospect.toString());
            } else {
                total++;
            }
        }
        System.out.println("Found data for " + total + " players.");
//		return participants;
    }

    //given a string, variable name (the field to be interpolated), and a value, perform some dirty interpolation
    private String interpolate(String string, String target, String value) {
        String interpolated = string.replaceAll(Pattern.quote("${" + target + "}"), value);
        return interpolated;
    }

    public void refreshAll(String importUUID, List<Player> players, ParserProgressMessage progress) throws IOException {

//        loadNflCom(importUUID, players, progress.init(10));

        loadNflDraftCountdown(2018, importUUID, players, progress.init(20));

        loadDraftTek(importUUID, players, progress.init(20));

        loadWalterFootballDraft(importUUID, players, progress.init(20));

//        loadCbsSportsDraft(importUUID, players, progress.init(10));
    }

    public void updateCombineResults(ParserProgressMessage progress) {
        this.getCombineWorkoutResults(progress);
        logger.debug("PAUSE");
    }

    public void loadNflCom(String importUUID, List<Player> players, ParserProgressMessage init) {
        List<College> allColleges = this.combineDao.allColleges();
        Map<String, JSONObject> map = loadDraftPicksJs();
        JSONObject prospectsJson = map.get("prospects");
        for (Object prospectId : prospectsJson.keySet()) {
            Player player = new Player();
            JSONObject prospectJson = prospectsJson.getJSONObject(prospectId.toString());
            player.setName(StringUtils.capitalize(prospectJson.getString("firstName").toLowerCase()) + " " + StringUtils.capitalize(prospectJson.getString("lastName").toLowerCase()));
            player.setCollege(prospectJson.getInt("college"));
            College college = this.playerService.findCollegeById(player.getCollege(), allColleges);
            player.setCollegeText(college.getName());
            Object weight = prospectJson.getDouble("weight");
            if (null != weight) {
                player.setWeight(Double.parseDouble(weight.toString()));
            }
            Object handSize = prospectJson.get("handSize");
            if (null != handSize) {
                player.setHandSize(Double.parseDouble(handSize.toString()));
            }
            addToPlayersList(player, players);
        }
    }


    public void loadNflDraftCountdown(int year, String importUUID, List<Player> players, ParserProgressMessage progress) throws IOException {

        int NAME_COL_IDX = 1;
        int COLLEGE_COL_IDX = 2;
        int HEIGHT_COL_IDX = 3;
        int WEIGHT_COL_IDX = 4;
        int FORTYTIME_COL_IDX = 5;
        String nflDraftCountdownURL = "http://www.draftcountdown.com/nfl-draft-ranking/" + year + "-";
        try {
            Map<String, String> positionsMap = this.combineImporterConversionService.mapOf(true, "QB", "quarterback", "RB", "running-back", "FB", "fullback", "WR", "wide-receiver", "TE", "tight-end", "OT", "offensive-tackle", "OG", "offensive-guard", "C", "center", "DE", "defensive-end", "DT", "defensive-tackle", "OLB", "outside-linebacker", "ILB", "inside-linebacker", "CB", "cornerback", "S", "safety", "LS", "long-snapper", "K", "kicker", "P", "punter", "RS", "return-specialist");
            double page = 1;
            for (String pageLink : positionsMap.keySet()) {
                String url = nflDraftCountdownURL + pageLink + "-rankings/";

                //set user-agent header
                HttpHeaders headers = new HttpHeaders();
                headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

                this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress.with(page, positionsMap.keySet().size(), "Importing DraftCountdown page " + page + " of " + positionsMap.keySet().size())));

                try {
                    //load page
                    ResponseEntity<String> response = restTemplate.exchange(
                            url, HttpMethod.GET, entity, String.class);

                    Document document = Jsoup.parse(response.getBody());
                    Elements playerRows = document.getElementsByClass("rank-table-container")
                            .get(0).getElementsByTag("tbody")
                            .get(0).getElementsByTag("tr");

                    //setup event object to be used as payload in progress messages
                    for (int i = 0; i < playerRows.size(); i++) {
                        Player player = new Player();
                        player.setSource("NFLDraftCountdown");
                        player.setPositionRank(i + 1);
                        player.setImportUUID(importUUID);
                        player.setYear(year);
                        player.setPosition(positionsMap.get(pageLink));

                        Elements dataElements = playerRows.get(i).getElementsByTag("td");
                        player.setName(dataElements.get(NAME_COL_IDX).getElementsByTag("a").get(0).html().replaceAll("[\\*’',]", ""));
                        if (player.getName().contains("’") || player.getName().contains("'") || player.getName().contains("*")) {
                            System.out.println("Pause");
                        }

                        //college
                        String collegeText = dataElements.get(COLLEGE_COL_IDX).html();
                        player.setCollege(this.combineImporterConversionService.collegeNameToId(collegeText));
                        player.setCollegeText(collegeText);

                        player.setHeight(this.combineImporterConversionService.toRawInches(dataElements.get(HEIGHT_COL_IDX).html()));
                        player.setWeight(Double.parseDouble(dataElements.get(WEIGHT_COL_IDX).html()));
                        player.setFortyYardDash(Double.parseDouble(dataElements.get(FORTYTIME_COL_IDX).html()));
                        addToPlayersList(player, players);

                    }
                } catch (HttpClientErrorException e) {
                    System.out.println("error: " + e.toString());
                }
                page++;
            }
        } catch (ExceptionInInitializerError e) {
            System.out.println("Unable to parse map: " + e.toString());
        }
    }

    private void addToPlayersList(Player player, List<Player> players) {
        Optional<Player> existingPlayer = players.stream().filter(p -> p.getName().toLowerCase().equals(player.getName().toLowerCase())).findFirst();
        if (!existingPlayer.isPresent()) {
            players.add(player);
        }
    }

    public Integer insertPlayers(List<Player> players, ParserProgressMessage progress) {

        this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress.with(1, 2, "Saving to database")));
        Integer result = this.playerService.addPlayers(players);
        this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress.with(2, 2, "Finished saving database")));
        return result;
    }

    public void loadWalterFootballDraft(String importUUID, List<Player> players, ParserProgressMessage progress) throws IOException {

        String walterFootballURL = "http://walterfootball.com/draft";
        String[] walterFootballPositions = new String[]{"QB", "RB", "FB", "WR", "TE", "OT", "OG", "C", "DE", "DT", "NT", "OLB3-4", "DE3-4", "OLB", "ILB", "CB", "S", "K", "P"};
        for (int year = 2018; year <= 2019; year++) {
            for (String position : walterFootballPositions) {

                String url = "";

                //random edge case
                if ("P".equals(position)) {
                    url = walterFootballURL + year + "PU" + ".php";
                } else {
                    url = walterFootballURL + year + position + ".php";
                }

                //set user-agent header
                HttpHeaders headers = new HttpHeaders();
                headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

                //load page
                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.GET, entity, String.class);

                //parse html & get player list as html elements
                Document doc = Jsoup.parse(response.getBody());
                Elements playerListItems = doc.getElementById("MainContentBlock").getElementsByTag("ol").get(0).getElementsByTag("li");

                this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress.with(year - 2017, 2, "Importing WalterFootball year " + (year - 2017) + " of 2")));

                //loop over each html player list item
                for (int i = 0; i < playerListItems.size(); i++) {
                    Player player = new Player();
                    player.setYear(year);
                    player.setImportUUID(importUUID);
                    player.setSource("WalterFootball");

                    Element playerElement = playerListItems.get(i);
                    try {
                        String playerDetails = playerElement.getElementsByTag("b").get(0).html();

                        String[] parts = playerDetails.split(",");
                        player.setName(parts[0].trim().replaceAll("[\\*’',]", ""));
                        player.setPosition(position.replaceAll("[^A-Z]", ""));
                        player.setPositionRank(i + 1);

                        String collegeText = parts[2].substring(0, parts[2].indexOf("<br>")).trim().replaceAll("\\.", "");
                        player.setCollege(this.combineImporterConversionService.collegeNameToId(collegeText));
                        player.setCollegeText(collegeText);

                        player.setHeight(this.combineImporterConversionService.toRawInches(this.combineImporterConversionService.parseTextParts(playerDetails, "Height: ", ".")));
                        player.setWeight(this.combineImporterConversionService.toRawInches(this.combineImporterConversionService.parseTextParts(playerDetails, "Weight: ", ".")));
                        player.setProjectedRound(this.combineImporterConversionService.parseTextParts(playerDetails, "Projected Round (" + year + "): ", "."));

                        try {
                            player.setFortyYardDash(Double.parseDouble(this.combineImporterConversionService.parseTextParts(playerDetails, "Projected 40 Time: ", 4)));
                        } catch (NumberFormatException e) {
                            System.out.println("No forty yard dash value found");
                        }
                        addToPlayersList(player, players);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }
        }
    }

    public void loadCbsSportsDraft(String importUUID, List<Player> players, ParserProgressMessage progress) throws IOException {

        int NAME_COL_IDX = 2;
        int COLLEGE_COL_IDX = 3;
        int CLASS_IDX = 4;
        int POSITION_IDX = 5;
        int POSITION_RANK_IDX = 6;
        int HEIGHT_COL_IDX = 7;
        int WEIGHT_COL_IDX = 8;
        String nflDraftCountdownURL = "https://www.cbssports.com/nfl/draft/prospect-rankings/";
        try {
            //set user-agent header
            HttpHeaders headers = new HttpHeaders();
            headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress.with(1, 1, "Importing DraftCountdown")));

            try {
                //load page
                ResponseEntity<String> response = restTemplate.exchange(
                        nflDraftCountdownURL, HttpMethod.GET, entity, String.class);

                Document document = Jsoup.parse(response.getBody());
                Element tableWrapper = document.getElementById("video-playlist");
                Elements playerRows = tableWrapper.getElementsByTag("tbody")
                        .get(0).getElementsByTag("tr");

                //setup event object to be used as payload in progress messages
                for (int i = 0; i < playerRows.size(); i++) {
                    Player player = new Player();
                    player.setSource("NFLDraftCountdown");
                    player.setRank(i + 1);
                    player.setImportUUID(importUUID);

                    Elements dataElements = playerRows.get(i).getElementsByTag("td");
                    player.setName(dataElements.get(NAME_COL_IDX).getElementsByTag("a").get(0).html().replaceAll("[\\*’',]", ""));
                    player.setYearClass(dataElements.get(CLASS_IDX).html().replaceAll("[\\*’',]", ""));

                    //position / position rank
                    player.setPosition(dataElements.get(POSITION_IDX).html().replaceAll("[ \\*’',]", ""));
                    player.setPositionRank(Integer.parseInt(dataElements.get(POSITION_RANK_IDX).html().replaceAll("[ \\*’',]", "")));

                    //college
                    String collegeText = dataElements.get(COLLEGE_COL_IDX).html();
                    player.setCollege(this.combineImporterConversionService.collegeNameToId(collegeText));
                    player.setCollegeText(collegeText);

                    player.setHeight(this.combineImporterConversionService.toRawInches(dataElements.get(HEIGHT_COL_IDX).html()));
                    player.setWeight(Double.parseDouble(dataElements.get(WEIGHT_COL_IDX).html()));
                    addToPlayersList(player, players);

                }
            } catch (HttpClientErrorException e) {
                System.out.println("error: " + e.toString());
            }
        } catch (ExceptionInInitializerError e) {
            System.out.println("Unable to parse map: " + e.toString());
        }
    }

    public void loadCombineDataForCbsSportsDraft(String importUUID, List<Player> players) {

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
                if (i == 191) {
                    System.out.println("pause");
                }
                String firstname = prospect.getString("firstName").replaceAll("[^a-zA-Z]", "");
                String lastname = prospect.getString("lastName").replaceAll("[^a-zA-Z]", "");
                String college = prospect.getString("college");
                String position = prospect.getString("position");
                String conference = prospect.getString("conference");

                Player p = this.combineImporterConversionService.findPlayerByNflData(firstname, lastname, college, conference, position);
                if (p != null) {
                    p.setFortyYardDash(this.jsonService.getDoubleFromJSON(prospect, "fortyYardDash"));
                    p.setBenchPress(this.jsonService.getDoubleFromJSON(prospect, "benchPress"));
                    p.setVerticalJump(this.jsonService.getDoubleFromJSON(prospect, "verticalJump"));
                    p.setBroadJump(this.jsonService.getDoubleFromJSON(prospect, "broadJump"));
                    p.setThreeConeDrill(this.jsonService.getDoubleFromJSON(prospect, "threeConeDrill"));
                    p.setTwentyYardShuttle(this.jsonService.getDoubleFromJSON(prospect, "twentyYardShuttle"));
                    p.setSixtyYardShuttle(this.jsonService.getDoubleFromJSON(prospect, "sixtyYardShuttle"));
                    p.setArmLength(this.combineImporterConversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "armLength")));
                    p.setHandSize(this.combineImporterConversionService.toRawInches(this.jsonService.getStringFromJSON(prospect, "handSize")));

                    inserted = this.combineDao.updateWorkoutResults(p) > 0;
                }

            } catch (Exception e) {
                logger.warn("Error adding participant: " + prospect.toString());
            }

            if (!inserted) {
                errors.add("unable to find match for: " + prospect.toString());
            }
        }

        System.out.println("pause...");

    }

    public void loadDraftTek(String importUUID, List<Player> players, ParserProgressMessage progress) throws IOException {

        List<Position> positions = this.combineDao.getPositions();

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1; i <= 3; i++) {

            this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress.with(i, 3, "Importing DraftTek page " + i + " of 3")));

            String url = (i > 1 ? DRAFT_TEK_PAGE + i : DRAFT_TEK) + ASP_EXT;
            Document doc = Jsoup.connect(url).get();

            Element wrapper = doc.getElementById("content");
            List<Element> rows = wrapper.getElementsByClass("BigBoardColor1");
            for (int k = 0; k < rows.size(); k++) {
                Element row = rows.get(k);
                List<Element> tdElements = row.getElementsByTag("td");
                Player player = new Player();
                player.setSource("DraftTek");
                for (int j = 0; j < tdElements.size(); j++) {
                    String value = tdElements.get(j).html();
                    if (!StringUtils.isEmpty(value)) {
                        switch (j) {
                            case 0:
                                player.setRank(Integer.parseInt(value));
                                break;
                            case 2:
                                player.setName(value.replaceAll("[\\*’',]", ""));
                                break;
                            case 3:
                                player.setCollege(this.combineImporterConversionService.collegeNameToId(value.replace("amp;", "")));
                                player.setCollegeText(value.replace("amp;", ""));
                                break;
                            case 4:
                                player.setPosition(getPositionDraftTek(value, positions));

                                //once the position is determined, generate the position rank
                                int positionRank = (int) (players.stream().filter(p -> player.getPosition().equals(p.getPosition())).count() + 1);
                                player.setPositionRank(positionRank);
                                break;
                            case 5:
                                player.setHeight(this.combineImporterConversionService.toRawInches(value));
                                break;
                            case 6:
                                player.setWeight(Double.parseDouble(value));
                                break;
                            default:
                                break;
                        }
                    }
                }

                if (players.stream().filter(p -> p.getName() == null || player.getName() == null || p.getName().toLowerCase().equals(player.getName().toLowerCase())).collect(Collectors.toList()).isEmpty()) {
                    player.setImportUUID(importUUID);
                    player.setYear(currentYear);
                    addToPlayersList(player, players);
                }
            }
        }
    }

    private String getPositionDraftTek(String position, List<Position> positionList) {
        Optional<Position> foundPosition = positionList.stream().filter(p -> position.equals(p.getName())).findFirst();
        if (foundPosition.isPresent()) {
            return foundPosition.get().getName();
        }

        //if drafttek specific position name, then map to corresponding position in db
        switch (position) {
            case "RBF":
            case "RBC":
                return "RB";
            case "WRF":
            case "WRS":
                return "WR";
            case "EDGE":
                return "OLB";
            case "DL1T":
                return "NT";
            case "DL3T":
                return "DT";
            case "DL5T":
                return "DE";
            case "OC":
                return "C";
            case "PK":
                return "K";
            default:
                System.out.println("Position not found");
                return "";
        }
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
