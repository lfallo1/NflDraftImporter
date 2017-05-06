package com.combine.profootballref.weekly.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.combine.profootballref.weekly.dto.GameScoringPlay;
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

/**
 * Service to retrieve data from Pro football reference
 * 
 * @author lancefallon
 *
 */
public class ProFootballRefService {
	
	String[] PLAY_TYPES = new String[]{"PASS","RUSH","PUNT","KOFF","ONSD","FG","XP","2PCR","2PCP"};
	String IS_COMPLETE = "&is_complete="; // passes
	String IS_SCORING_PLAY = "&is_scoring="; // any
	String IS_FIRSTDOWN = "&is_first_down="; //passes / rushes
	String IS_SACK = "&is_sack="; //passes
	String IS_TURNOVER = "&is_turnover="; // any
	String TURNOVER_TYPE = "&turnover_type="; // tunovers
	int[] TRUE_FALSE_OPTIONS = new int[]{0,1}; 
	
	
	/**
	 * PlayTypes:
PASS,RUSH,PUNT,KOFF,ONSD,FG,XP,2PCR,2PCP


RUSH:
LE
LT
LG
M
RG
RT
RE

PASS:
SL
SM
SR
DL
DM
DR
	 */

	public static final String SEASON_TYPE_REGULAR = "R";
	public static final String SEASON_TYPE_PLAYOFFS = "P";

	Logger LOGGER = Logger.getLogger(ProFootballRefService.class);

	// HTML CONSTANTS
	private static final String ELEMENT_TD = "td";
	private static final String ELEMENT_TH = "th";
	private static final String ELEMENT_TR = "tr";
	private static final String RESULTS_TABLE = "results";
	private static final String PLAY_BY_PLAY_TABLE = "pbp";
	private static final String SCORING_SUMMARY_TABLE = "scoring";
	private static final String TEAMS_ACTIVE_TABLE = "teams_active";
	private static final String TEAMS_INACTIVE_TABLE = "teams_inactive";

	// MISC CONSTANTS
	private static final String YEAR = "year";
	private static final String GAME_TYPE = "gameType";

	// URL CONSTANTS
	private static final String PRO_FOOTBALL_REF_BASE_URL = "http://www.pro-football-reference.com";
	private static final String PRO_FOOTBALL_REF_WEEKLY_QB = PRO_FOOTBALL_REF_BASE_URL
			+ "/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=25&age_min=0&age_max=9999&game_type=${gameType}&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=25&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=pass_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=pass_rating&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RUSHING = PRO_FOOTBALL_REF_BASE_URL
			+ "/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=25&age_min=0&age_max=9999&game_type=${gameType}&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=25&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rush_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rush_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RECEIVING = PRO_FOOTBALL_REF_BASE_URL
			+ "/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=25&age_min=0&age_max=9999&game_type=${gameType}&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=25&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rec&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rec_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_DEFENSE = PRO_FOOTBALL_REF_BASE_URL
			+ "/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&age_min=0&pos=0&league_id=&opp_id=&game_type=${gameType}&game_num_min=0&game_num_max=25&week_num_min=1&week_num_max=25&stadium_id=&game_day_of_week=&game_month=&c1stat=tackles_solo&c1comp=gt&c2stat=def_int&c2comp=gt&c3stat=sacks&c3comp=gt&c4comp=gt&c5comp=choose&c5gtlt=lt&c6mult=1.0&c6comp=choose&order_by=sacks";
	private static final String PRO_FOOTBALL_REF_WEEKLY_BOX_SCORE = PRO_FOOTBALL_REF_BASE_URL
			+ "/play-index/tgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&game_type=${gameType}&game_num_min=0&game_num_max=25&week_num_min=0&week_num_max=25&team_conf_id=All+Conferences&team_div_id=All+Divisions&opp_conf_id=All+Conferences&opp_div_id=All+Divisions&team_off_scheme=Any+Scheme&team_def_align=Any+Alignment&opp_off_scheme=Any+Scheme&opp_def_align=Any+Alignment&c1stat=quarter_1_score_tgl&c1comp=gt&c2stat=pass_cmp&c2comp=gt&c3stat=first_down&c3comp=gt&c4stat=penalties&c4comp=gt&c5comp=rush_att&c5gtlt=gte&c6mult=0&c6comp=turnovers&order_by=game_date&order_by_asc=Y";
	private static final String PRO_FOOTBALL_REF_TEAMS = PRO_FOOTBALL_REF_BASE_URL + "/teams/";
	private static final String OFFSET = "&offset=";
	private static final String PRO_FOOTBALL_PLAYBYPLAY = "http://www.pro-football-reference.com/play-index/play_finder.cgi?request=1&super_bowl=0&match=all&game_num_min=0&game_num_max=99&quarter=1&quarter=2&quarter=3&quarter=4&quarter=5&tr_gtlt=lt&minutes=15&seconds=00&down=0&down=1&down=2&down=3&down=4&yg_gtlt=gt&is_first_down=-1&field_pos_min_field=team&field_pos_max_field=team&end_field_pos_min_field=team&end_field_pos_max_field=team&score_type=safety&is_sack=-1&include_kneels=0&no_play=0&order_by=game_date";
	private static final String PLAY_BY_PLAY_TABLE_ALT = "all_plays";

	private TableMapperService tableMapperService;
	private GenericsService genericsService;
	private HttpService httpService;
	private DataConversionService dataConversionService;

	public ProFootballRefService(TableMapperService tableMapperService, GenericsService genericsService,
			HttpService httpService, DataConversionService dataConversionService) {
		this.tableMapperService = tableMapperService;
		this.genericsService = genericsService;
		this.httpService = httpService;
		this.dataConversionService = dataConversionService;
	}

	public List<WeeklyStatsPassing> loadWeeklyStatsPassing(String gameType, List<Team> teams, Integer year) {
		System.out.println("loading passing stats");
		return this.<WeeklyStatsPassing> loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_QB, gameType, WeeklyStatsPassing.class,
				teams, year);
	}

	public List<WeeklyStatsRushing> loadWeeklyStatsRushing(String gameType, List<Team> teams, Integer year) {
		System.out.println("loading rushing stats");
		return this.<WeeklyStatsRushing> loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_RUSHING, gameType,
				WeeklyStatsRushing.class, teams, year);
	}

	public List<WeeklyStatsReceiving> loadWeeklyStatsReceiving(String gameType, List<Team> teams, Integer year) {
		System.out.println("loading receiving stats");
		return this.<WeeklyStatsReceiving> loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_RECEIVING, gameType,
				WeeklyStatsReceiving.class, teams, year);
	}

	public List<WeeklyStatsDefense> loadWeeklyStatsDefense(String gameType, List<Team> teams, Integer year) {
		System.out.println("loading defense stats");
		return this.<WeeklyStatsDefense> loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_DEFENSE, gameType,
				WeeklyStatsDefense.class, teams, year);
	}

	public List<Team> loadAllTeams() {
		List<Team> teams = new ArrayList<>();
		teams.addAll(this.loadTeams(TEAMS_ACTIVE_TABLE));
		teams.addAll(this.loadTeams(TEAMS_INACTIVE_TABLE));
		return teams;
	}

	public List<WeeklyStatsGame> loadWeeklyStatsGames(String gameType, List<Team> teams, Integer year) {
		System.out.println("loading weekly team stats");
		List<WeeklyStatsGame> weeklyStats = this.<WeeklyStatsGame> loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_BOX_SCORE,
				gameType, WeeklyStatsGame.class, teams, year);
		return weeklyStats;
	}

	public List<Game> getUniqueGamesList(List<WeeklyStatsGame> weeklyStats) {
		return new ArrayList<>(this.dataConversionService.getUniqueListGames(weeklyStats, PRO_FOOTBALL_REF_BASE_URL));
	}

	public List<WeeklyStatsIndividualPlay> getPlayByPlay(List<Game> games, List<Team> teams) {
		List<WeeklyStatsIndividualPlay> plays = new ArrayList<>();
		int counter = 1;
		for (Game game : games) {
			System.out.println("loading play by play for game " + (counter++) + " of " + games.size());
			plays.addAll(loadPlayByPlay(game));
		}
		return plays;
	}

	public List<GameScoringPlay> getScoringSummaries(List<Game> games, List<Team> teams) {
		List<GameScoringPlay> scoringPlays = new ArrayList<>();
		int counter = 1;
		for (Game game : games) {
			scoringPlays.addAll(loadScoringSummaries(game, teams));
			System.out.println("loading scoring summary for game " + (counter++) + " of " + games.size());
		}
		return scoringPlays;
	}

	/**
	 * generic loader to retrieve weekly stats from pro football reference
	 * 
	 * @param baseUrl
	 * @param clazz
	 * @return
	 */
	private <T extends WeeklyStats> List<T> loadWeeklyStats(String baseUrl, String gameType, Class<T> clazz,
			List<Team> teams, Integer year) {
		Map<Integer, String> headers = new HashMap<>();
		List<T> results = new ArrayList<>();

		String url = baseUrl;
		int page = 0;
		
		//while a next page exists, get all the records
		while (true) {

			// set the url (append the offset query string if not on first
			// page), and then increment page count
			if (page > 0) {
				url = baseUrl + OFFSET + page * 100;
			}
			page++;

			try {

				// make request & verify data returned
				Map<String, String> interpolationMap = new HashMap<>();
				interpolationMap.put(YEAR, String.valueOf(year));
				interpolationMap.put(GAME_TYPE, gameType);
				String interpolatedUrl = genericsService.interpolate(url, interpolationMap);
				List<Element> rows = loadTableRowsByIdentifier(interpolatedUrl, RESULTS_TABLE);

				// immediately exit page loop once there is no data left.
				// for weekly stats, the table has two header rows. that is why we're checking if less than 3
				if (rows.size() < 3)
					break;

				// if header row has not been created, do it here
				if (headers.size() == 0) {
					headers = tableMapperService.parseTableHeaderRow(rows.get(1).getElementsByTag(ELEMENT_TH));
				}

				// for all other rows, convert the record into an object and
				// add to list
				for (int j = 2; j < rows.size(); j++) {
					Element row = rows.get(j);
					List<Element> tdElements = row.getElementsByTag(ELEMENT_TD);

					// ensure the row has elements (only <td> elements
					// qualify. if it's a random <th> row, the row is
					// skipped)
					if (tdElements.size() > 0) {
						// parse and add the object
						T result = genericsService.createInstance(clazz);
						tableMapperService.parseTableRow(headers, tdElements, result, 1, false);
						setIdentifiers(row, result); // set the player &
														// game identifiers
														// / links
						result.setTeamObject(
								this.dataConversionService.findByTeamIdentifier(result.getTeamIdentifier(), teams));
						result.setOpponentObject(this.dataConversionService
								.findByTeamIdentifier(result.getOpponentIdentifier(), teams));

						// set the score props
						result.setSeasonType(gameType);
						setScoreProps(result);
						results.add(result);
					}
				}
				System.out.println("Iteration summary: Current Year=> " + year + ", Current Page: " + page
						+ ", total results: " + results.size());
			} catch (InstantiationException | IllegalAccessException | IOException e) {
				LOGGER.log(Level.WARN, e.getMessage());
			}
		}

		return results;
	}

	/**
	 * load the scoring summary table (each score with a brief description and
	 * updated score)
	 * 
	 * @param game
	 */
	private List<GameScoringPlay> loadScoringSummaries(Game game, List<Team> teams) {
		List<GameScoringPlay> scoringSummary = new ArrayList<>();
		Map<Integer, String> headers = new HashMap<>();
		try {
			// make request
			String url = PRO_FOOTBALL_REF_BASE_URL + game.getGameLink();
			List<Element> rows = loadTableRowsByIdentifier(url, SCORING_SUMMARY_TABLE);

			// set headers
			headers = tableMapperService.parseTableHeaderRow(rows.get(0).getElementsByTag(ELEMENT_TH));

			// the quarter in the table only displays once (the first score of
			// any given quarter), and is blank otherwise.
			// this holds the value of the previous quarter for cases when it is
			// returned as empty.
			int previousQuarter = -1;

			// for all other rows, convert the record into an object and add to
			// list
			for (int j = 1; j < rows.size(); j++) {
				Element row = rows.get(j);
				Elements tdElements = new Elements();

				// need to explicitly add the quarter element because it is a
				// header instead of data row
				Element quarterElement = row.getElementsByTag(ELEMENT_TH).first();
				tdElements.add(quarterElement);
				tdElements.addAll(row.getElementsByTag(ELEMENT_TD));

				// ensure the row has elements (only <td> elements qualify. if
				// it's a random <th> row, the row is skipped)
				if (tdElements.size() > 0) {
					// parse and add the object
					GameScoringPlay gameScore = new GameScoringPlay();
					tableMapperService.parseTableRow(headers, tdElements, gameScore, 0, true);

					// handle the quarter shenanigens
					if (gameScore.getQuarter() == null) {
						gameScore.setQuarter(previousQuarter);
					} else {
						previousQuarter = gameScore.getQuarter();
					}
					gameScore.setGameIdentifier(game.getGameIdentifier());
					gameScore
							.setTeamObject(dataConversionService.findByTeamName(gameScore.getScoringTeamName(), teams));
					scoringSummary.add(gameScore);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scoringSummary;
	}
	
	public void loadPlayByPlay(List<Team> teams, WeeklyNflStatsDal weeklyNflStatsDal){
		String base = "http://www.pro-football-reference.com/play-index/play_finder.cgi?request=1&match=all&game_num_min=0&game_num_max=998&quarter=1&quarter=2&quarter=3&quarter=4&quarter=5&tr_gtlt=lt&minutes=15&seconds=00&down=0&down=1&down=2&down=3&down=4&yg_gtlt=gt&is_first_down=-1&field_pos_min_field=team&field_pos_max_field=team&end_field_pos_min_field=team&end_field_pos_max_field=team&is_complete=-1&turnover_type=interception&turnover_type=fumble&score_type=touchdown&score_type=field_goal&score_type=safety&is_sack=-1&include_kneels=0&no_play=0&order_by=game_date&more_options=0&pass_location=SL&pass_location=SM&pass_location=SR&pass_location=DL&pass_location=DM&pass_location=DR";
		for(Team team : teams){
			//years
			for(int year = 1994; year < 2016; year++){
				//weeks
				for(int week = 1; week < 21; week++){
					String gameType = week < 18 ? "R" : "P";
					List<WeeklyStatsIndividualPlay> plays = new ArrayList<>();
					int isSuperBowl = week == 21 ? 1 : 0;
					for(String playType : PLAY_TYPES){
						plays.addAll(this.loadPlayByPlay(base, team.getTeamIdentifier(), gameType, year, week, playType, -1, -1, isSuperBowl));
//						//runs / passes
//						if(playType.equals("PASS") || playType.equals("RUSH")){
//							for(int isTurnover : TRUE_FALSE_OPTIONS){
//								for(int isScore : TRUE_FALSE_OPTIONS){
//									this.loadPlayByPlay(base, team.getTeamIdentifier(), gameType, year, week, playType, isTurnover, isScore);
//								}
//							}
//						//special teams
//						} else{
//							this.loadPlayByPlay(base, team.getTeamIdentifier(), gameType, year, week, playType, -1, -1);
//						}						
					}
					int inserted = weeklyNflStatsDal.addGamePlays(plays);
					System.out.println("inserted " + inserted + " plays (" + team.getTeamIdentifier() + " " + year + " week#" + week + ")");
				}
			}
		}
	}

	/**
	 * team_id, game_type, year_min, year_max, week_num_min, week_num_max, type, rush_direction, pass_direction
	 * @param team
	 * @param year
	 * @param week
	 * @param gameType
	 * @param playType
	 * @param playDirection
	 * @return
	 */
	private List<WeeklyStatsIndividualPlay> loadPlayByPlay(String urlBase, String team, String gameType, 
			Integer year, Integer week, String playType, int isTurnover, int isScore, int isSuperBowl) {
		List<WeeklyStatsIndividualPlay> plays = new ArrayList<>();
		Map<Integer, String> headers = new HashMap<>();
		try {
			String url = urlBase + "&team_id=" + team + "&game_type=" + gameType + "&year_min=" + year +
					"&year_max=" + year + "&week_num_min=" + week + "&week_num_max=" + week + "&type=" + playType + "&is_turnover=" + isTurnover + "&is_score=" + isScore + "&isSuperBowl=" + isSuperBowl;
//			// make request
//			if(playType.equals("RUSH")){
//				url+="&rush_direction=" + playDirection;
//			} else if(playType.equals("PASS")){
//				url+="&pass_location=" + playDirection;
//			}
			
			List<Element> rows = loadTableRowsByIdentifier(url, PLAY_BY_PLAY_TABLE_ALT);
			if(rows.size() > 0){
				headers = tableMapperService.parseTableHeaderRow(rows.get(0).getElementsByTag(ELEMENT_TH));
	
				// for all other rows, convert the record into an object and add to
				// list
				for (int j = 1; j < rows.size(); j++) {
					Element row = rows.get(j);
					Elements tdElements = new Elements();
	
					// need to explicitly add the quarter element because it is a
					// header instead of data row
					Element quarterElement = row.getElementsByTag(ELEMENT_TH).first();
					tdElements.add(quarterElement);
					tdElements.addAll(row.getElementsByTag(ELEMENT_TD));
	
					// ensure the row has elements (only <td> elements qualify. if
					// it's a random <th> row, the row is skipped)
					if (tdElements.size() > 0) {
						// parse and add the object
						WeeklyStatsIndividualPlay play = new WeeklyStatsIndividualPlay();
						tableMapperService.parseTableRow(headers, tdElements, play, 0, false);
						analyzePlayDetails(tdElements, "detail", play); // parse out
																		// play
																		// description
																		// into
																		// individual
																		// properties
//						if (!StringUtils.isEmpty(play.getDescription()) && play.getDescription().indexOf("timeout") < 0) {
							play.setGameIdentifier(play.getGameIdentifier());
							play.setPlayTypeString(playType);
							plays.add(play);
//						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return plays;
	}
	
	/**
	 * load the play by play stats of a game
	 * 
	 * @param game
	 */
	private List<WeeklyStatsIndividualPlay> loadPlayByPlay(Game game) {
		List<WeeklyStatsIndividualPlay> plays = new ArrayList<>();
		Map<Integer, String> headers = new HashMap<>();
		try {
			// make request
			String url = PRO_FOOTBALL_REF_BASE_URL + game.getGameLink();
			List<Element> rows = loadTableRowsByIdentifier(url, PLAY_BY_PLAY_TABLE);
			if(rows.size() > 0){
				headers = tableMapperService.parseTableHeaderRow(rows.get(0).getElementsByTag(ELEMENT_TH));
	
				// for all other rows, convert the record into an object and add to
				// list
				for (int j = 2; j < rows.size(); j++) {
					Element row = rows.get(j);
					Elements tdElements = new Elements();
	
					// need to explicitly add the quarter element because it is a
					// header instead of data row
					Element quarterElement = row.getElementsByTag(ELEMENT_TH).first();
					tdElements.add(quarterElement);
					tdElements.addAll(row.getElementsByTag(ELEMENT_TD));
	
					// ensure the row has elements (only <td> elements qualify. if
					// it's a random <th> row, the row is skipped)
					if (tdElements.size() > 0) {
						// parse and add the object
						WeeklyStatsIndividualPlay play = new WeeklyStatsIndividualPlay();
						tableMapperService.parseTableRow(headers, tdElements, play, 0, false);
						analyzePlayDetails(tdElements, "detail", play); // parse out
																		// play
																		// description
																		// into
																		// individual
																		// properties
						if (!StringUtils.isEmpty(play.getDescription()) && play.getDescription().indexOf("timeout") < 0) {
							play.setGameIdentifier(game.getGameIdentifier());
							plays.add(play);
						}
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
		Map<Integer, String> headers = new HashMap<>();
		try {
			// make request
			List<Element> rows = loadTableRowsByIdentifier(PRO_FOOTBALL_REF_TEAMS, tableIdentifier).stream()
					.filter(r -> !r.hasClass("partial_table")).collect(Collectors.toList());

			// set headers
			headers = tableMapperService.parseTableHeaderRow(rows.get(1).getElementsByTag(ELEMENT_TH));

			// for all other rows, convert the record into an object and add to
			// list
			for (int j = 2; j < rows.size(); j++) {
				Element row = rows.get(j);
				Elements tdElements = new Elements();

				// need to explicitly add the first element because it is a
				// header instead of data row
				Element teamNameElement = row.getElementsByTag(ELEMENT_TH).first();
				tdElements.add(teamNameElement);
				tdElements.addAll(row.getElementsByTag(ELEMENT_TD));

				// ensure the row has elements (only <td> elements qualify. if
				// it's a random <th> row, the row is skipped)
				if (tdElements.size() > 0) {
					// parse and add the object
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
	 * Given a row (list of of td elements) & an attribute value specifying the
	 * element with the play description, get the data and parse out the play
	 * information. Currently only parses out the play type, description, and
	 * yards (if play from scrimmage)
	 * 
	 * @param tdElements
	 * @param attributeValue
	 * @param play
	 */
	private void analyzePlayDetails(Elements tdElements, String attributeValue, WeeklyStatsIndividualPlay play) {

		// get the element with a data-stat attribute value equal to the
		// attributeValue param
		Optional<Element> teamElement = tdElements.stream().filter(e -> e != null
				&& !StringUtils.isEmpty(e.attr("data-stat")) && e.attr("data-stat").equals("team")).findFirst();
		Optional<Element> oppElement = tdElements.stream().filter(e -> e != null
				&& !StringUtils.isEmpty(e.attr("data-stat")) && e.attr("data-stat").equals("opp")).findFirst();
		Optional<Element> gameIdElement = tdElements.stream().filter(e -> e != null
				&& !StringUtils.isEmpty(e.attr("data-stat")) && e.attr("data-stat").equals("game_date")).findFirst();
		if (teamElement.isPresent() && oppElement.isPresent()) {
			String teamText = teamElement.get().getElementsByTag("a").get(0).attr("href");
			String teamIdentifier = teamText.substring(teamText.lastIndexOf("/")-3,teamText.length()-1);
			String oppText = oppElement.get().getElementsByTag("a").get(0).attr("href");
			String oppIdentifier = oppText.substring(oppText.lastIndexOf("/")-3,oppText.length()-1);
			play.setTeam(teamIdentifier);
			play.setOpp(oppIdentifier);
			
			String gameIdText = gameIdElement.get().getElementsByTag("a").get(0).attr("href");
			play.setGameIdentifier(gameIdText.substring(gameIdText.lastIndexOf("/")+1, gameIdText.lastIndexOf(".")));
		}
		
		//set the score
		String[] scoreParts = play.getScore().split("-");
		play.setTeamScore(Integer.parseInt(scoreParts[0]));
		play.setOppScore(Integer.parseInt(scoreParts[1]));
		
		play.setYardsGained(play.getYardsGained() == null ? 0 : play.getYardsGained());
		
		int locationInt = 0;
		
		try{
			locationInt = Integer.parseInt(play.getLocation().replaceAll("[^\\d.]", ""));
			if(play.getLocation().startsWith(play.getTeam().toUpperCase())){
				locationInt = locationInt * -1;
			}
		} catch(NullPointerException e){
			//TODO add handler
		}
		play.setLocationInt(locationInt);

		// if an element is present, loop over the child nodes and parse the
		// play description
//		StringBuilder playDescription = new StringBuilder();
//		for (int i = 1; i < element.get().childNodes().size(); i++) {
//			Node node = element.get().childNodes().get(i);
//			if (node.childNodes().size() > 0) {
//				playDescription.append(" " + node.childNode(0).toString() + " ");
//			} else {
//				playDescription.append(node.toString().trim());
//			}
//		}

		// simple string wrapper to make some code more concise
//		StringWrapper playDesc = new StringWrapper(playDescription.toString().trim().toLowerCase());

		// add play description to IndividualPlayDetails object
//		play.setDescription(playDesc.get());

//		// determine type of play and set yards gained if applicable
//		if (playDescription.indexOf("timeout") > -1) {
//			return;
//		} else if (playDesc.has("field goal")) {
//			play.setPlayType(PlayType.FIELD_GOAL);
//		} else if (playDesc.has("extra point")) {
//			play.setPlayType(PlayType.EXTRA_POINT);
//		} else if (playDesc.has("punts")) {
//			play.setPlayType(PlayType.PUNT);
//		} else if (playDesc.has("kicks off")) {
//			play.setPlayType(PlayType.KICKOFF);
//		} else if (playDesc.has("two point")) {
//			play.setPlayType(PlayType.TWO_POINT);
//		} else {
//			play.setYardsGained(0); // initialize to zero yards in case of
//									// incomplete pass / no gain where yards is
//									// not specified in the description
//			if (playDesc.has("pass complete") || playDesc.has("pass incomplete") || playDesc.has("sacked")) {
//				play.setPlayType(PlayType.PASS);
//			} else {
//				// a rush includes kneels
//				play.setPlayType(PlayType.RUN);
//			}
//
//			Pattern p1 = Pattern.compile("\\bfor (-?\\d{1,3}) \\byard");
//			Matcher m1 = p1.matcher(playDesc.get());
//			if (m1.find()) {
//				play.setYardsGained(Integer.valueOf(m1.group(1)));
//			}
//		}
	}

	/**
	 * given a weekly stats object, set the player (optional) and game
	 * identifiers
	 * 
	 * @param row
	 * @param result
	 */
	private <T extends WeeklyStats> void setIdentifiers(Element row, T result) {
		Optional<Element> playerElement = row.getElementsByAttribute("data-stat").stream()
				.filter(e -> e.attr("data-stat").equals("player")).findFirst();

		Optional<Element> gameElement = row.getElementsByAttribute("data-stat").stream()
				.filter(e -> e.attr("data-stat").equals("game_date")).findFirst();

		Optional<Element> teamElement = row.getElementsByAttribute("data-stat").stream()
				.filter(e -> e.attr("data-stat").equals("team")).findFirst();

		Optional<Element> opponentElement = row.getElementsByAttribute("data-stat").stream()
				.filter(e -> e.attr("data-stat").equals("opp")).findFirst();

		if (playerElement.isPresent()) {
			String playerIdentifier = playerElement.get().attr("data-append-csv");
			String playerLink = playerElement.get().getElementsByTag("a").attr("href");
			result.setPlayerIdentifier(playerIdentifier);
			result.setPlayerLink(playerLink);
		}

		if (gameElement.isPresent()) {
			String gameLink = gameElement.get().getElementsByTag("a").attr("href");
			String gameIdentifier = gameLink.substring(gameLink.lastIndexOf("/") + 1, gameLink.indexOf("."));
			result.setGameLink(gameLink);
			result.setGameIdentifier(gameIdentifier);
		}

		if (teamElement.isPresent()) {
			String teamLink = teamElement.get().getElementsByTag("a").attr("href");
			String teamIdentifier = teamLink.replace("/teams/", "").substring(0, 3);
			result.setTeamIdentifier(teamIdentifier);
		}

		if (opponentElement.isPresent()) {
			String opponentLink = opponentElement.get().getElementsByTag("a").attr("href");
			String opponentIdentifier = opponentLink.replace("/teams/", "").substring(0, 3);
			result.setOpponentIdentifier(opponentIdentifier);
		}
	}

	private void setTeamIdentifiers(Element row, Team team) {

		Optional<Element> teamElement = row.getElementsByAttribute("data-stat").stream()
				.filter(e -> e.attr("data-stat").equals("team_name")).findFirst();

		if (teamElement.isPresent()) {
			String teamLink = teamElement.get().getElementsByTag("a").attr("href");
			String teamIdentifier = teamLink.replace("/teams/", "").substring(0, 3);
			team.setTeamLink(teamLink);
			team.setTeamIdentifier(teamIdentifier);
		}
	}

	/**
	 * given a url, fetch the html document and return the specified table by
	 * the element id
	 * 
	 * @param url
	 * @param elementId
	 * @return
	 * @throws IOException
	 */
	private List<Element> loadTableRowsByIdentifier(String url, String elementId) throws IOException {
		Document doc = httpService.getDocumentFromUrl(url);
		if (doc == null) {
			return new ArrayList<>();
		}

		// load the table
		Element table = doc.getElementById(elementId);
		if (table == null) {
			return new ArrayList<>();
		}

		List<Element> rows = table.getElementsByTag(ELEMENT_TR);
		return rows;
	}
	
	/**
	 * given a WeeklyStats object, set the score properties by splitting at the dash.
	 * team for whom stats were generated is considered the "team", while the other team is "opponent"
	 * @param result
	 */
	public <T extends WeeklyStats> void setScoreProps(T result) {
		String[] parts = result.getResult().substring(2).split("-");
		result.setTeamScore(Integer.parseInt(parts[0]));
		result.setOppScore(Integer.parseInt(parts[1]));
	}

}
