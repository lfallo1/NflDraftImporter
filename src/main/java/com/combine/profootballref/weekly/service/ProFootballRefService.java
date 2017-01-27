package com.combine.profootballref.weekly.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.combine.profootballref.weekly.dto.GameScoringPlay;
import com.combine.profootballref.weekly.dto.PlayType;
import com.combine.profootballref.weekly.dto.WeeklyStats;
import com.combine.profootballref.weekly.dto.WeeklyStatsDefense;
import com.combine.profootballref.weekly.dto.WeeklyStatsGame;
import com.combine.profootballref.weekly.dto.WeeklyStatsIndividualPlay;
import com.combine.profootballref.weekly.dto.WeeklyStatsPassing;
import com.combine.profootballref.weekly.dto.WeeklyStatsReceiving;
import com.combine.profootballref.weekly.dto.WeeklyStatsRushing;
import com.combine.profootballref.weekly.model.Game;
import com.combine.profootballref.weekly.model.Team;
import com.combine.service.GenericsService;
import com.combine.service.HttpService;
import com.combine.service.TableMapperService;
import com.combine.util.StringWrapper;

/**
 * Service to retrieve data from Pro football reference
 * @author lancefallon
 *
 */
public class ProFootballRefService {

	public static final String SEASON_TYPE_REGULAR = "R";
	public static final String SEASON_TYPE_PLAYOFFS = "P";
	
	Logger LOGGER = Logger.getLogger(ProFootballRefService.class);
	
	//HTML CONSTANTS
	private static final String ELEMENT_TD = "td";
	private static final String ELEMENT_TH = "th";
	private static final String ELEMENT_TR = "tr";
	private static final String RESULTS_TABLE = "results";
	private static final String PLAY_BY_PLAY_TABLE = "pbp";
	private static final String SCORING_SUMMARY_TABLE = "scoring";
	private static final String TEAMS_ACTIVE_TABLE = "teams_active";
	private static final String TEAMS_INACTIVE_TABLE = "teams_inactive";
	
	//MISC CONSTANTS
	private static final String YEAR = "year";
	private static final String GAME_TYPE = "gameType";
//	private static final int YEAR_END = 1950;
	private static final int YEAR_END = 2012;
	private static final int YEAR_START = 2012;
	
	//URL CONSTANTS
	private static final String PRO_FOOTBALL_REF_BASE_URL = "http://www.pro-football-reference.com";
	private static final String PRO_FOOTBALL_REF_WEEKLY_QB = PRO_FOOTBALL_REF_BASE_URL + "/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=25&age_min=0&age_max=9999&game_type=${gameType}&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=25&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=pass_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=pass_rating&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RUSHING = PRO_FOOTBALL_REF_BASE_URL + "/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=25&age_min=0&age_max=9999&game_type=${gameType}&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=25&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rush_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rush_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RECEIVING = PRO_FOOTBALL_REF_BASE_URL + "/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=25&age_min=0&age_max=9999&game_type=${gameType}&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=25&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rec&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rec_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_DEFENSE = PRO_FOOTBALL_REF_BASE_URL + "/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&age_min=0&pos=0&league_id=&opp_id=&game_type=${gameType}&game_num_min=0&game_num_max=25&week_num_min=1&week_num_max=25&stadium_id=&game_day_of_week=&game_month=&c1stat=tackles_solo&c1comp=gt&c2stat=def_int&c2comp=gt&c3stat=sacks&c3comp=gt&c4comp=gt&c5comp=choose&c5gtlt=lt&c6mult=1.0&c6comp=choose&order_by=sacks";
	private static final String PRO_FOOTBALL_REF_WEEKLY_BOX_SCORE = PRO_FOOTBALL_REF_BASE_URL + "/play-index/tgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&game_type=${gameType}&game_num_min=0&game_num_max=25&week_num_min=0&week_num_max=25&team_conf_id=All+Conferences&team_div_id=All+Divisions&opp_conf_id=All+Conferences&opp_div_id=All+Divisions&team_off_scheme=Any+Scheme&team_def_align=Any+Alignment&opp_off_scheme=Any+Scheme&opp_def_align=Any+Alignment&c1stat=quarter_1_score_tgl&c1comp=gt&c2stat=pass_cmp&c2comp=gt&c3stat=first_down&c3comp=gt&c4stat=penalties&c4comp=gt&c5comp=rush_att&c5gtlt=gte&c6mult=0&c6comp=turnovers&order_by=game_date&order_by_asc=Y";
	private static final String PRO_FOOTBALL_REF_TEAMS = PRO_FOOTBALL_REF_BASE_URL + "/teams/";
	private static final String OFFSET = "&offset=";
	
	private TableMapperService tableMapperService;
	private GenericsService genericsService;
	private HttpService httpService;
	private DataConversionService dataConversionService;
	
	public ProFootballRefService(TableMapperService tableMapperService, GenericsService genericsService, HttpService httpService, DataConversionService dataConversionService){
		this.tableMapperService = tableMapperService;
		this.genericsService = genericsService;
		this.httpService = httpService;
		this.dataConversionService = dataConversionService;
	}
	
	public List<WeeklyStatsPassing> loadWeeklyStatsPassing(String gameType, List<Team> teams){
		return this.<WeeklyStatsPassing>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_QB, gameType, WeeklyStatsPassing.class, teams);
	}
	
	public List<WeeklyStatsRushing> loadWeeklyStatsRushing(String gameType, List<Team> teams){
		return this.<WeeklyStatsRushing>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_RUSHING, gameType, WeeklyStatsRushing.class, teams);
	}
	
	public List<WeeklyStatsReceiving> loadWeeklyStatsReceiving(String gameType, List<Team> teams){
		return this.<WeeklyStatsReceiving>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_RECEIVING, gameType, WeeklyStatsReceiving.class, teams);
	}
	
	public List<WeeklyStatsDefense> loadWeeklyStatsDefense(String gameType, List<Team> teams){
		return this.<WeeklyStatsDefense>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_DEFENSE, gameType, WeeklyStatsDefense.class, teams);
	}
	
	public List<Team> loadAllTeams(){
		List<Team> teams = new ArrayList<>();
		teams.addAll(this.loadTeams(TEAMS_ACTIVE_TABLE));
		teams.addAll(this.loadTeams(TEAMS_INACTIVE_TABLE));
		return teams;
	}
	
	public List<WeeklyStatsGame> loadWeeklyStatsGames(String gameType, List<Team> teams){
		List<WeeklyStatsGame> weeklyStats = this.<WeeklyStatsGame>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_BOX_SCORE, gameType, WeeklyStatsGame.class, teams);
		return weeklyStats;
	}
	
	public List<Game> getUniqueGamesList(List<WeeklyStatsGame> weeklyStats){
		return new ArrayList<>(this.dataConversionService.getUniqueListGames(weeklyStats));
	}
	
	public List<WeeklyStatsIndividualPlay> getPlayByPlay(List<Game> games, List<Team> teams){
		List<WeeklyStatsIndividualPlay> plays = new ArrayList<>();
		for(Game game : games){
			plays.addAll(loadPlayByPlay(game));
		}
		return plays;
	}
	
	public List<GameScoringPlay> getScoringSummaries(List<Game> games, List<Team> teams){
		List<GameScoringPlay> scoringPlays = new ArrayList<>();
		for(Game game : games){
			scoringPlays.addAll(loadScoringSummaries(game, teams));
		}
		return scoringPlays;
	}
	
	

	/**
	 * generic loader to retrieve weekly stats from pro football reference
	 * @param baseUrl
	 * @param clazz
	 * @return
	 */
	private <T extends WeeklyStats> List<T> loadWeeklyStats(String baseUrl, String gameType, Class<T> clazz, List<Team> teams){
		Map<Integer,String> headers = new HashMap<>();
		List<T> results = new ArrayList<>();
		
		for(int i = YEAR_START; i >= YEAR_END; i--){
			String url = baseUrl;
			int page = 0;
			while(true){
				
				//set the url (append the offset query string if not on first page), and then increment page count
				if(page > 0){
					url = baseUrl + OFFSET + page * 100; 
				}
				page++;
				
				try {
					
					//make request & verify data returned
					Map<String, String> interpolationMap = new HashMap<>();
					interpolationMap.put(YEAR, String.valueOf(i));
					interpolationMap.put(GAME_TYPE, gameType);
					String interpolatedUrl = genericsService.interpolate(url, interpolationMap);
					List<Element> rows = loadTableRowsByIdentifier(interpolatedUrl, RESULTS_TABLE);
					
					//immediately exit page loop once there is no data left
					if(rows.size() < 3)
						break;
					
					//if header row has not been created, do it here
					if(headers.size() == 0){
						headers = tableMapperService.parseTableHeaderRow(rows.get(1).getElementsByTag(ELEMENT_TH));
					}
					
					//for all other rows, convert the record into an object and add to list
					for(int j = 2; j  <rows.size(); j++){
						Element row = rows.get(j);
						List<Element> tdElements = row.getElementsByTag(ELEMENT_TD);
						
						//ensure the row has elements (only <td> elements qualify. if it's a random <th> row, the row is skipped)
						if(tdElements.size() > 0){
							//parse and add the object
							T result = genericsService.createInstance(clazz);
							tableMapperService.parseTableRow(headers, tdElements, result, 1, false);
							setIdentifiers(row,result); //set the player & game identifiers / links
							result.setTeamObject(this.dataConversionService.findByTeamIdentifier(result.getTeamIdentifier(), teams));
							result.setOpponentObject(this.dataConversionService.findByTeamIdentifier(result.getOpponentIdentifier(), teams));
							result.setSeasonType(gameType);
							results.add(result);
						}
					}
					System.out.println("Iteration summary: Current Year=> " + i +", Current Page: " + page + ", total results: " + results.size());
				} catch (InstantiationException | IllegalAccessException | IOException e) {
					LOGGER.log(Level.WARN, e.getMessage());
				}
			}			
		}
		return results;
	}
	
	/**
	 * load the scoring summary table (each score with a brief description and updated score)
	 * @param game
	 */
	private List<GameScoringPlay> loadScoringSummaries(Game game, List<Team> teams) {
		List<GameScoringPlay> scoringSummary = new ArrayList<>();
		Map<Integer,String> headers = new HashMap<>();
		try {
			//make request
			String url = PRO_FOOTBALL_REF_BASE_URL + game.getGameLink();
			List<Element> rows = loadTableRowsByIdentifier(url, SCORING_SUMMARY_TABLE);
			
			//set headers
			headers = tableMapperService.parseTableHeaderRow(rows.get(0).getElementsByTag(ELEMENT_TH));
			
			//the quarter in the table only displays once (the first score of any given quarter), and is blank otherwise. 
			//this holds the value of the previous quarter for cases when it is returned as empty.
			int previousQuarter = -1;
			
			//for all other rows, convert the record into an object and add to list
			for(int j = 1; j  <rows.size(); j++){
				Element row = rows.get(j);
				Elements tdElements = new Elements();
				
				//need to explicitly add the quarter element because it is a header instead of data row
				Element quarterElement = row.getElementsByTag(ELEMENT_TH).first();
				tdElements.add(quarterElement);
				tdElements.addAll(row.getElementsByTag(ELEMENT_TD));
				
				//ensure the row has elements (only <td> elements qualify. if it's a random <th> row, the row is skipped)
				if(tdElements.size() > 0){
					//parse and add the object
					GameScoringPlay gameScore = new GameScoringPlay();
					tableMapperService.parseTableRow(headers, tdElements, gameScore, 0, true);
					
					//handle the quarter shenanigens
					if(gameScore.getQuarter() == null){
						gameScore.setQuarter(previousQuarter);
					} else{
						previousQuarter = gameScore.getQuarter();
					}
					gameScore.setGameIdentifier(game.getGameIdentifier());
					gameScore.setTeamObject(dataConversionService.findByTeamName(gameScore.getScoringTeamName(), teams));
					scoringSummary.add(gameScore);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scoringSummary;
	}
	
	/**
	 * load the play by play stats of a game
	 * @param game
	 */
	private List<WeeklyStatsIndividualPlay> loadPlayByPlay(Game game){
		List<WeeklyStatsIndividualPlay> plays = new ArrayList<>();
		Map<Integer,String> headers = new HashMap<>();
		try {
			//make request
			String url = PRO_FOOTBALL_REF_BASE_URL + game.getGameLink();
			List<Element> rows = loadTableRowsByIdentifier(url, PLAY_BY_PLAY_TABLE);
			
			headers = tableMapperService.parseTableHeaderRow(rows.get(0).getElementsByTag(ELEMENT_TH));
			
			//for all other rows, convert the record into an object and add to list
			for(int j = 2; j  <rows.size(); j++){
				Element row = rows.get(j);
				Elements tdElements = new Elements();
				
				//need to explicitly add the quarter element because it is a header instead of data row
				Element quarterElement = row.getElementsByTag(ELEMENT_TH).first();
				tdElements.add(quarterElement);
				tdElements.addAll(row.getElementsByTag(ELEMENT_TD));
				
				//ensure the row has elements (only <td> elements qualify. if it's a random <th> row, the row is skipped)
				if(tdElements.size() > 0){
					//parse and add the object
					WeeklyStatsIndividualPlay play = new WeeklyStatsIndividualPlay();
					tableMapperService.parseTableRow(headers, tdElements, play, 0, false);
					analyzePlayDetails(tdElements, "detail", play); //parse out play description into individual properties
					if(!StringUtils.isEmpty(play.getDescription()) && play.getDescription().indexOf("timeout") < 0){
						play.setGameIdentifier(game.getGameIdentifier());
						plays.add(play);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return plays;
	}
	
	private List<Team> loadTeams(String tableIdentifier) {
		List<Team> teams = new ArrayList<>();
		Map<Integer,String> headers = new HashMap<>();
		try {
			//make request
			List<Element> rows = loadTableRowsByIdentifier(PRO_FOOTBALL_REF_TEAMS, tableIdentifier)
						.stream()
						.filter(r->!r.hasClass("partial_table"))
						.collect(Collectors.toList());
			
			//set headers
			headers = tableMapperService.parseTableHeaderRow(rows.get(1).getElementsByTag(ELEMENT_TH));
			
			//for all other rows, convert the record into an object and add to list
			for(int j = 2; j  <rows.size(); j++){
				Element row = rows.get(j);
				Elements tdElements = new Elements();
				
				//need to explicitly add the first element because it is a header instead of data row
				Element teamNameElement = row.getElementsByTag(ELEMENT_TH).first();
				tdElements.add(teamNameElement);
				tdElements.addAll(row.getElementsByTag(ELEMENT_TD));
				
				//ensure the row has elements (only <td> elements qualify. if it's a random <th> row, the row is skipped)
				if(tdElements.size() > 0){
					//parse and add the object
					Team team = new Team();
					tableMapperService.parseTableRow(headers, tdElements, team, 0, true);
					setTeamIdentifiers(row, team);
					teams.add(team);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return teams;
	}
	
	/**
	 * Given a row (list of of td elements) & an attribute value specifying the element with the play description, get the data and parse out the play information.
	 * Currently only parses out the play type, description, and yards (if play from scrimmage)
	 * @param tdElements
	 * @param attributeValue
	 * @param play
	 */
	private void analyzePlayDetails(Elements tdElements, String attributeValue, WeeklyStatsIndividualPlay play) {
		
		//get the element with a data-stat attribute value equal to the attributeValue param
		Optional<Element> element = tdElements.stream()
				.filter(e-> e != null && !StringUtils.isEmpty(e.attr("data-stat")) && e.attr("data-stat").equals(attributeValue))
				.findFirst();
		if(!element.isPresent()){
			return;
		}
		
		//if an element is present, loop over the child nodes and parse the play description
		StringBuilder playDescription = new StringBuilder();
		for(int i = 1; i < element.get().childNodes().size(); i++){
			Node node = element.get().childNodes().get(i);
			if(node.childNodes().size() > 0){
				playDescription.append(" " + node.childNode(0).toString() + " ");
			} else{
				playDescription.append(node.toString().trim());
			}
		}
		
		//simple string wrapper to make some code more concise
		StringWrapper playDesc = new StringWrapper(playDescription.toString().trim().toLowerCase());
		
		//add play description to IndividualPlayDetails object
		play.setDescription(playDesc.get());
		
		//determine type of play and set yards gained if applicable
		if(playDescription.indexOf("timeout") > -1){
			return;
		}
		else if(playDesc.has("field goal")){
			play.setPlayType(PlayType.FIELD_GOAL);
		} else if(playDesc.has("extra point")){
			play.setPlayType(PlayType.EXTRA_POINT);
		} else if(playDesc.has("punts")){
			play.setPlayType(PlayType.PUNT);
		} else if(playDesc.has("kicks off")){
			play.setPlayType(PlayType.KICKOFF);
		} else if(playDesc.has("two point")){
			play.setPlayType(PlayType.TWO_POINT);
		} else{
			play.setYardsGained(0); //initialize to zero yards in case of incomplete pass / no gain where yards is not specified in the description
			if(playDesc.has("pass complete") || playDesc.has("pass incomplete") || playDesc.has("sacked")){
				play.setPlayType(PlayType.PASS);
			} else{
				//a rush includes kneels
				play.setPlayType(PlayType.RUN);
			}
			
			Pattern p1 = Pattern.compile("\\bfor (-?\\d{1,3}) \\byard");
			Matcher m1 = p1.matcher(playDesc.get());
			if(m1.find()){
				play.setYardsGained(Integer.valueOf(m1.group(1)));
			}			
		}
	}	

	/**
	 * given a weekly stats object, set the player (optional) and game identifiers
	 * @param row
	 * @param result
	 */
	private <T extends WeeklyStats>void setIdentifiers(Element row, T result) {
		Optional<Element> playerElement = row.getElementsByAttribute("data-stat")
					.stream()
					.filter(e->e.attr("data-stat").equals("player"))
					.findFirst();
		
		Optional<Element> gameElement = row.getElementsByAttribute("data-stat")
				.stream()
				.filter(e->e.attr("data-stat").equals("game_date"))
				.findFirst();
		
		Optional<Element> teamElement = row.getElementsByAttribute("data-stat")
				.stream()
				.filter(e->e.attr("data-stat").equals("team"))
				.findFirst();

		Optional<Element> opponentElement = row.getElementsByAttribute("data-stat")
				.stream()
				.filter(e->e.attr("data-stat").equals("opp"))
				.findFirst();
		
		if(playerElement.isPresent()){
			String playerIdentifier = playerElement.get().attr("data-append-csv");
			String playerLink = playerElement.get().getElementsByTag("a").attr("href");
			result.setPlayerIdentifier(playerIdentifier);
			result.setPlayerLink(playerLink);
		}
		
		if(gameElement.isPresent()){
			String gameLink = gameElement.get().getElementsByTag("a").attr("href");
			String gameIdentifier = gameLink.substring(gameLink.lastIndexOf("/")+1, gameLink.indexOf("."));
			result.setGameLink(gameLink);
			result.setGameIdentifier(gameIdentifier);
		}
		
		if(teamElement.isPresent()){
			String teamLink = teamElement.get().getElementsByTag("a").attr("href");
			String teamIdentifier = teamLink.replace("/teams/", "").substring(0, 3);
			result.setTeamIdentifier(teamIdentifier);
		}
		
		if(opponentElement.isPresent()){
			String opponentLink = opponentElement.get().getElementsByTag("a").attr("href");
			String opponentIdentifier = opponentLink.replace("/teams/", "").substring(0, 3);
			result.setOpponentIdentifier(opponentIdentifier);
		}
	}
	
	private void setTeamIdentifiers(Element row, Team team) {
		
		Optional<Element> teamElement = row.getElementsByAttribute("data-stat")
				.stream()
				.filter(e->e.attr("data-stat").equals("team_name"))
				.findFirst();
		
		if(teamElement.isPresent()){
			String teamLink = teamElement.get().getElementsByTag("a").attr("href");
			String teamIdentifier = teamLink.replace("/teams/", "").substring(0, 3);
			team.setTeamLink(teamLink);
			team.setTeamIdentifier(teamIdentifier);
		}
	}
	
	/**
	 * given a url, fetch the html document and return the specified table by the element id
	 * @param url
	 * @param elementId
	 * @return
	 * @throws IOException
	 */
	private List<Element> loadTableRowsByIdentifier(String url, String elementId) throws IOException {
		Document doc = httpService.getDocumentFromUrl(url);
		if(doc == null){
			return new ArrayList<>();
		}
		
		//load the table
		Element table = doc.getElementById(elementId);
		if(table == null){
			return new ArrayList<>();
		}
		
		List<Element> rows = table.getElementsByTag(ELEMENT_TR);
		return rows;
	}
	
}
