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

public class ProFootballRefService {
	
	private static final String OFFSET = "&offset=";
	private static final String PRO_FOOTBALL_REF_WEEKLY_QB = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=18&age_min=0&age_max=99&game_type=A&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=99&week_num_min=1&week_num_max=1&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=pass_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=pass_rating&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RUSHING = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=18&age_min=0&age_max=99&game_type=A&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=99&week_num_min=1&week_num_max=1&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rush_att&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rush_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_RECEIVING = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=18&age_min=0&age_max=99&game_type=A&league_id=&team_id=&opp_id=&game_num_min=0&game_num_max=99&week_num_min=1&week_num_max=1&game_day_of_week=&game_location=&game_result=&handedness=&is_active=&is_hof=&c1stat=rec&c1comp=gt&c1val=1&c2stat=&c2comp=gt&c2val=&c3stat=&c3comp=gt&c3val=&c4stat=&c4comp=gt&c4val=&order_by=rec_yds&from_link=1";
	private static final String PRO_FOOTBALL_REF_WEEKLY_DEFENSE = "http://www.pro-football-reference.com/play-index/pgl_finder.cgi?request=1&match=game&year_min=${year}&year_max=${year}&season_start=1&season_end=-1&week_num_min=1&week_num_max=18&age_min=0&age_max=0&pos=0&game_type=R&career_game_num_min=0&career_game_num_max=499&game_num_min=0&game_num_max=99&week_num_min=1&week_num_max=1&c1stat=tackles_solo&c1comp=gt&c1val=0&c2stat=def_int&c2comp=gt&c2val=0&c3stat=choose&c3comp=gt&c4stat=choose&c4comp=gt&c5comp=choose&c5gtlt=lt&c6mult=1.0&c6comp=choose";
	
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

	private <T> List<T> loadWeeklyStats(String baseUrl, Class<T> clazz){
		Map<Integer,String> headers = new HashMap<>();
		List<T> results = new ArrayList<>();
		for(int i = 2016; i >= 1960; i--){
			
			int page = 0;
			while(true){
				
				//set the url (append the offset query string if not on first page)
				String url = baseUrl;
				if(page > 0){
					url = baseUrl + OFFSET + page * 100; 
				}
				page++;
				
				try {
					
					
					//make request and get table
					Document doc = Jsoup.connect(genericService.interpolate(url, "year", String.valueOf(i))).get();
					Element table = doc.getElementById("results");
					List<Element> rows = table.getElementsByTag("tr");
					
					//immediately exit page loop once there is no data left
					if(rows.size() < 3)
						break;
					
					//if header row has not been created, do it here
					if(headers.size() == 0){
						headers = tableMapperService.parseTableHeaderRow(rows.get(1).getElementsByTag("th"));
					}
					
					for(int j = 2; j  <rows.size(); j++){
						Element row = rows.get(j);
						List<Element> tdElements = row.getElementsByTag("td");
						
						if(tdElements.size() > 0){
							T result = genericService.createInstance(clazz);
							tableMapperService.parseTableRow(headers, tdElements, result);
							results.add(result);
						}
					}
					
				} catch (IOException | InstantiationException | IllegalAccessException e) {
					System.out.println("No data for " + url);
				}	
			}			
		}
		return results;
	}
	
}
