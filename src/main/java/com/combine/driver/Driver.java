package com.combine.driver;

import java.io.IOException;

import com.combine.dal.DataSourceLayer;
import com.combine.service.ParserService;

public class Driver {

	public static void main(String[] args) throws IOException{		
		DataSourceLayer dataSource = DataSourceLayer.getInstance();
		ParserService parser = new ParserService(dataSource);
		parser.loadCombineDataForCbsSportsDraft();
		
		//static/site/7.4/scripts/combine/participants.js --> dataSrc = new Y2.util.DataSource(playersDataSet);
		
//		parser.insertColleges();
//		parser.parse();
		
//		ParserService parser = new ParserService();
//		parser.loadCbsSportsDraft();
//		parser.loadDraftTek();
		
//		JSONService jsonService = new JSONService();
//		jsonService.jsonToExcel("fatracker.json", "/Users/lancefallon/Desktop/FreeAgents2016.xls", "freeAgents", Arrays.asList("videoId", "contentId", "gsisPlayerId", "ex", "th", "id", "rankOrder", "playerId", "lastUpdate", "analysis"));
	}
	
}
