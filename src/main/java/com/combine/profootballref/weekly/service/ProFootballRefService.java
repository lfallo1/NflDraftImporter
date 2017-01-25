package com.combine.profootballref.weekly.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.combine.profootballref.weekly.model.WeeklyStatsDefense;
import com.combine.profootballref.weekly.model.WeeklyStatsQuarterback;
import com.combine.profootballref.weekly.model.WeeklyStatsReceiving;
import com.combine.profootballref.weekly.model.WeeklyStatsRushing;
import com.combine.service.GenericService;
import com.combine.service.TableMapperService;

/**
 * Service to retrieve data from Pro football reference
 * @author lancefallon
 *
 */
public class ProFootballRefService {
	
	//HTML CONSTANTS
	private static final String ELEMENT_TD = "td";
	private static final String ELEMENT_TH = "th";
	private static final String ELEMENT_TR = "tr";
	private static final String ELEMENT_ID_RESULTS = "results";
	
	//MISC CONSTANTS
	private static final String YEAR = "year";
	private static final int YEAR_END = 1960;
	private static final int YEAR_START = 2016;
	
	//URL CONSTANTS
	private static final String OFFSET = "&offset=";
	private static final String PRO_FOOTBALL_REF_WEEKLY_QB = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=18&age_min=0&age_max=99&game_type=A&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=99&week_num_min=1&week_num_max=1&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=pass_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=pass_rating&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RUSHING = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=18&age_min=0&age_max=99&game_type=A&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=99&week_num_min=1&week_num_max=1&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rush_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rush_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RECEIVING = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=18&age_min=0&age_max=99&game_type=A&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=99&week_num_min=1&week_num_max=1&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rec&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rec_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_DEFENSE = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=18&age_min=0&age_max=0&pos=0&game_type=R&career_game_num_min=0&career_game_num_max=499&game_num_min=0&game_num_max=99&week_num_min=1&week_num_max=1&c1stat=tackles_solo&c1comp=gt&c1val=0&c2stat=def_int&c2comp=gt&c2val=0&c3stat=choose&c3comp=gt&c4stat=choose&c4comp=gt&c5comp=choose&c5gtlt=lt&c6mult=1.0&c6comp=choose";
	private static final int URL_REQUEST_ATTEMPTS = 3;
	
	private TableMapperService tableMapperService;
	private GenericService genericService;
	
	public ProFootballRefService(TableMapperService tableMapperService, GenericService genericService){
		this.tableMapperService = tableMapperService;
		this.genericService = genericService;
	}
	
	public List<WeeklyStatsQuarterback> loadWeeklyStatsQuarterback(){
		return this.<WeeklyStatsQuarterback>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_QB, WeeklyStatsQuarterback.class);
	}
	
	public List<WeeklyStatsRushing> loadWeeklyStatsRushing(){
		return this.<WeeklyStatsRushing>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_RUSHING, WeeklyStatsRushing.class);
	}
	
	public List<WeeklyStatsReceiving> loadWeeklyStatsReceiving(){
		return this.<WeeklyStatsReceiving>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_RECEIVING, WeeklyStatsReceiving.class);
	}
	
	public List<WeeklyStatsDefense> loadWeeklyStatsDefense(){
		return this.<WeeklyStatsDefense>loadWeeklyStats(PRO_FOOTBALL_REF_WEEKLY_DEFENSE, WeeklyStatsDefense.class);
	}

	/**
	 * generic loader to retrieve weekly stats from pro football reference
	 * @param baseUrl
	 * @param clazz
	 * @return
	 */
	private <T> List<T> loadWeeklyStats(String baseUrl, Class<T> clazz){
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
					Document doc = makeUrlRequest(genericService.interpolate(url, YEAR, String.valueOf(i)));
					if(doc == null) 
						continue;
					
					//load the table
					Element table = doc.getElementById(ELEMENT_ID_RESULTS);
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
							tableMapperService.parseTableRow(headers, tdElements, result);
							results.add(result);
						}
					}
					
				} catch (InstantiationException | IllegalAccessException | IOException e) {
					System.out.println("No data, check logs");
				}	
			}			
		}
		return results;
	}

	private Document makeUrlRequest(String url) throws IOException {
		Document doc = null;
		int attempt = 0;
		while(attempt < URL_REQUEST_ATTEMPTS){
			try{
				doc = Jsoup.connect(url).get();
				return doc;
			} catch(IOException e){
				attempt++;
			}
		}
		throw new IOException("Unable to fetch " + url);
	}
	
}
