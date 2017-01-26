package com.combine.driver;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;

import com.combine.profootballref.weekly.model.WeeklyStatsTeam;
import com.combine.profootballref.weekly.service.ProFootballRefService;
import com.combine.service.GenericService;
import com.combine.service.TableMapperService;

public class Driver {

	public static void main(String[] args) throws IOException {
		GenericService genericService = new GenericService();
		TableMapperService tableMapperService = new TableMapperService(genericService);
		ProFootballRefService proFootballRefService = new ProFootballRefService(tableMapperService, genericService);
		
		List<WeeklyStatsTeam> teamStats = proFootballRefService.loadWeeklyStatsTeam(ProFootballRefService.SEASON_TYPE_PLAYOFFS);
//		List<WeeklyStatsPassing> passing = proFootballRefService.loadWeeklyStatsPassing(ProFootballRefService.SEASON_TYPE_REGULAR);
		JSONArray jsonArray = new JSONArray(teamStats);
		System.out.println(jsonArray.toString());
	}

}
