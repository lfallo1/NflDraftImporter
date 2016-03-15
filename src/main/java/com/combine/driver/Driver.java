package com.combine.driver;

import java.io.IOException;
import java.util.Arrays;

import com.combine.service.JSONService;

public class Driver {

	public static void main(String[] args) throws IOException{		
//		DataSourceLayer dataSource = DataSourceLayer.getInstance();
//		ParserService parser = new ParserService(dataSource);		
//		parser.insertColleges();
//		parser.parse();
		
		JSONService jsonService = new JSONService();
		jsonService.jsonToExcel("fatracker.json", "/Users/lancefallon/Desktop/FreeAgents2016.xls", "freeAgents", Arrays.asList("videoId", "contentId", "gsisPlayerId", "ex", "th", "id", "rankOrder", "playerId", "lastUpdate", "analysis"));
	}
	
}
