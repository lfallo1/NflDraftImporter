package com.combine.driver;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;

import com.combine.profootballref.weekly.dto.Team;
import com.combine.profootballref.weekly.dto.WeeklyStatsDefense;
import com.combine.profootballref.weekly.dto.WeeklyStatsPassing;
import com.combine.profootballref.weekly.dto.WeeklyStatsReceiving;
import com.combine.profootballref.weekly.dto.WeeklyStatsRushing;
import com.combine.profootballref.weekly.service.DataConversionService;
import com.combine.profootballref.weekly.service.ProFootballRefService;
import com.combine.service.GenericsService;
import com.combine.service.HttpService;
import com.combine.service.TableMapperService;

public class Driver {

	public static void main(String[] args) throws IOException {
		DataConversionService dataConversionService = new DataConversionService();
		GenericsService genericsService = new GenericsService();
		HttpService httpService = new HttpService();
		TableMapperService tableMapperService = new TableMapperService(genericsService);
		ProFootballRefService proFootballRefService = new ProFootballRefService(tableMapperService, genericsService, httpService, dataConversionService);
		
		List<Team> teams = proFootballRefService.loadAllTeams();
//		List<WeeklyStatsGame> gameStats = proFootballRefService.loadWeeklyStatsGames(ProFootballRefService.SEASON_TYPE_PLAYOFFS, teams);
		List<WeeklyStatsPassing> passing = proFootballRefService.loadWeeklyStatsPassing(ProFootballRefService.SEASON_TYPE_REGULAR, teams);
//		List<WeeklyStatsRushing> rushing = proFootballRefService.loadWeeklyStatsRushing(ProFootballRefService.SEASON_TYPE_PLAYOFFS, teams);
//		List<WeeklyStatsReceiving> receiving = proFootballRefService.loadWeeklyStatsReceiving(ProFootballRefService.SEASON_TYPE_PLAYOFFS, teams);
//		List<WeeklyStatsDefense> defense = proFootballRefService.loadWeeklyStatsDefense(ProFootballRefService.SEASON_TYPE_PLAYOFFS, teams);
		System.out.println("pause...");
//		JSONArray jsonArray = new JSONArray(passing);
//		System.out.println(jsonArray.toString());
		
	}
}
