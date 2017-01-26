package com.combine.profootballref.weekly.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.combine.profootballref.weekly.model.WeeklyStats;
import com.combine.profootballref.weekly.model.WeeklyStatsDefense;
import com.combine.profootballref.weekly.model.WeeklyStatsIndividualPlay;
import com.combine.profootballref.weekly.model.WeeklyStatsPassing;
import com.combine.profootballref.weekly.model.WeeklyStatsReceiving;
import com.combine.profootballref.weekly.model.WeeklyStatsRushing;
import com.combine.profootballref.weekly.model.WeeklyStatsTeam;
import com.combine.service.GenericService;
import com.combine.service.TableMapperService;

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
	
	//MISC CONSTANTS
	private static final String YEAR = "year";
	private static final String GAME_TYPE = "gameType";
	private static final int YEAR_END = 2012;
	private static final int YEAR_START = 2012;
	
	//URL CONSTANTS
	private static final String PRO_FOOTBALL_REF_BASE_URL = "http://www.pro-football-reference.com";
	private static final String PRO_FOOTBALL_REF_WEEKLY_QB = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=9999&age_min=0&age_max=9999&game_type=${gameType}&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=9999&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=pass_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=pass_rating&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RUSHING = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=9999&age_min=0&age_max=9999&game_type=${gameType}&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=9999&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rush_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rush_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RECEIVING = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=9999&age_min=0&age_max=9999&game_type=${gameType}&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=9999&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rec&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rec_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_DEFENSE = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&age_min=0&pos=0&league_id=&opp_id=&game_type=${gameType}&game_num_min=0&game_num_max=9999&week_num_min=1&week_num_max=9999&stadium_id=&game_day_of_week=&game_month=&c1stat=tackles_solo&c1comp=gt&c2stat=def_int&c2comp=gt&c3stat=sacks&c3comp=gt&c4comp=gt&c5comp=choose&c5gtlt=lt&c6mult=1.0&c6comp=choose&order_by=sacks";
	private static final String PRO_FOOTBALL_REF_WEEKLY_BOX_SCORE = "http://www.pro-football-reference.com/play-index/tgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&game_type=${gameType}&game_num_min=0&game_num_max=9999&week_num_min=0&week_num_max=9999&team_conf_id=All+Conferences&team_div_id=All+Divisions&opp_conf_id=All+Conferences&opp_div_id=All+Divisions&team_off_scheme=Any+Scheme&team_def_align=Any+Alignment&opp_off_scheme=Any+Scheme&opp_def_align=Any+Alignment&c1stat=quarter_1_score_tgl&c1comp=gt&c2stat=pass_cmp&c2comp=gt&c3stat=first_down&c3comp=gt&c4stat=penalties&c4comp=gt&c5comp=rush_att&c5gtlt=gte&c6mult=0&c6comp=turnovers&order_by=game_date&order_by_asc=Y";
	private static final String OFFSET = "&offset=";
	private static final int URL_REQUEST_ATTEMPTS = 5;
	
	private TableMapperService tableMapperService;
	private GenericService genericService;
	
	public ProFootballRefService(TableMapperService tableMapperService, GenericService genericService){
		this.tableMapperService = tableMapperService;
		this.genericService = genericService;
	}
	
	public List<WeeklyStatsPassing> loadWeeklyStatsPassing(String gameType){
		return this.<WeeklyStatsPassing>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_QB, gameType, WeeklyStatsPassing.class);
	}
	
	public List<WeeklyStatsRushing> loadWeeklyStatsRushing(String gameType){
		return this.<WeeklyStatsRushing>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_RUSHING, gameType, WeeklyStatsRushing.class);
	}
	
	public List<WeeklyStatsReceiving> loadWeeklyStatsReceiving(String gameType){
		return this.<WeeklyStatsReceiving>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_RECEIVING, gameType, WeeklyStatsReceiving.class);
	}
	
	public List<WeeklyStatsDefense> loadWeeklyStatsDefense(String gameType){
		return this.<WeeklyStatsDefense>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_DEFENSE, gameType, WeeklyStatsDefense.class);
	}
	
	public List<WeeklyStatsTeam> loadWeeklyStatsTeam(String gameType){
		return this.loadWeeklyStatsTeam(gameType, false);
	}
	
	public List<WeeklyStatsTeam> loadWeeklyStatsTeam(String gameType, boolean includePlayByPlay){
		List<WeeklyStatsTeam> weeklyStats = this.<WeeklyStatsTeam>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_BOX_SCORE, gameType, WeeklyStatsTeam.class);
		
		if(includePlayByPlay){
			weeklyStats.stream().forEach(s->loadPlayByPlay(s));
		}
		
		return weeklyStats;
	}

	private void loadPlayByPlay(WeeklyStatsTeam game){
		List<WeeklyStatsIndividualPlay> plays = new ArrayList<>();
		Map<Integer,String> headers = new HashMap<>();
		try {
			//make request
			String url = PRO_FOOTBALL_REF_BASE_URL + game.getGameLink();
			Document doc = makeUrlRequest(url);
			
			//load the table
			Element table = doc.getElementById(PLAY_BY_PLAY_TABLE);
			List<Element> rows = table.getElementsByTag(ELEMENT_TR);
			
			//if header row has not been created, do it here
			if(headers.size() == 0){
				headers = tableMapperService.parseTableHeaderRow(rows.get(0).getElementsByTag(ELEMENT_TH));
			}
			
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
					tableMapperService.parseTableRow(headers, tdElements, play, 0);
					plays.add(play);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		game.setPlays(plays);
	}
	
	/**
	 * generic loader to retrieve weekly stats from pro football reference
	 * @param baseUrl
	 * @param clazz
	 * @return
	 */
	private <T extends WeeklyStats> List<T> loadWeeklyStats(String baseUrl, String gameType, Class<T> clazz){
		Map<Integer,String> headers = new HashMap<>();
		List<T> results = new ArrayList<>();
		for(int i = YEAR_START; i >= YEAR_END; i--){
			
			int page = 0;
			while(true){
				
				//set the url (append the offset query string if not on first page), and then increment page count
				String url = baseUrl;
				if(page > 0){
					url = baseUrl + OFFSET + page * 100; 
				}
				page++;
				
				try {
					
					//make request & verify data returned
					Map<String, String> interpolationMap = new HashMap<>();
					interpolationMap.put(YEAR, String.valueOf(i));
					interpolationMap.put(GAME_TYPE, gameType);
					Document doc = makeUrlRequest(genericService.interpolate(url, interpolationMap));
					if(doc == null) 
						continue;
					
					//load the table
					Element table = doc.getElementById(RESULTS_TABLE);
					if(table == null)
						break;
					
					List<Element> rows = table.getElementsByTag(ELEMENT_TR);
					
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
							T result = genericService.createInstance(clazz);
							tableMapperService.parseTableRow(headers, tdElements, result, 1);
							setIdentifiers(row,result); //set the player & game identifiers / links
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

	private <T extends WeeklyStats>void setIdentifiers(Element row, T result) {
		Optional<Element> playerElement = row.getElementsByAttribute("data-stat")
					.stream()
					.filter(e->e.attr("data-stat").equals("player"))
					.findFirst();
		
		Optional<Element> gameElement = row.getElementsByAttribute("data-stat")
				.stream()
				.filter(e->e.attr("data-stat").equals("game_date"))
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
	}

	private Document makeUrlRequest(String url) throws IOException {
		int attempt = 0;
		while(attempt < URL_REQUEST_ATTEMPTS){
			try{
				Document doc = Jsoup.connect(url).get();
				
				//in this step, uncomment html (this logic could change, but currently some tables / data are commented out on the website and unrecognized by document parser)
				String formatted = doc.toString().replaceAll("<!-- ", "");
				formatted = formatted.toString().replaceAll(" -->", "");
				return Jsoup.parse(formatted);
			} catch(IOException e){
				attempt++;
				System.out.println("retrying " + url);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					throw new IOException("Unable to fetch " + url);
				}
			}
		}
		throw new IOException("Unable to fetch " + url);
	}
	
}
