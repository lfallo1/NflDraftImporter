package com.combine.driver;

import java.io.IOException;
import java.util.List;

import com.combine.profootballref.weekly.model.WeeklyStatsPassing;
import com.combine.profootballref.weekly.model.WeeklyStatsRushing;
import com.combine.profootballref.weekly.service.ProFootballRefService;
import com.combine.service.GenericService;
import com.combine.service.TableMapperService;

public class Driver {

	public static void main(String[] args) throws IOException {
		GenericService genericService = new GenericService();
		TableMapperService tableMapperService = new TableMapperService(genericService);
		ProFootballRefService proFootballRefService = new ProFootballRefService(tableMapperService, genericService);
		List<WeeklyStatsPassing> passing = proFootballRefService.loadWeeklyStatsPassing();
		List<WeeklyStatsRushing> rushing = proFootballRefService.loadWeeklyStatsRushing();
		
		System.out.println("Passing stats retrieved: " + passing.size());
		System.out.println("Rushing stats retrieved: " + rushing.size());
	}

}
