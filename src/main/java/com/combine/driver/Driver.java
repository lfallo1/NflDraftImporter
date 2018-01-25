package com.combine.driver;

import com.combine.dal.DataSourceLayer;
import com.combine.service.ParserService;

import java.io.IOException;

public class Driver {

    public static void main(String[] args) throws IOException {

        DataSourceLayer dataSource = DataSourceLayer.getInstance();
        ParserService parser = new ParserService(dataSource);
//		parser.loadCombineDataForCbsSportsDraft();
//		parser.retrieveParticipants();
//		parser.updateDraftPicks();

        //static/site/7.4/scripts/combine/participants.js --> dataSrc = new Y2.util.DataSource(playersDataSet);
        //http://www.nfl.com/draft/<<Year>>/tracker#dt-tabs:dt-by-round --- if(typeof nfl.global.dt.writer !== 'undefined'){ ... nfl.draft.tracker.data.*
        // Object.keys(jsonArray).length;

//		parser.insertColleges();
//		parser.parse();

//		ParserService parser = new ParserService();
        parser.loadNflDraftCountdown(2018);
        parser.loadWalterFootballDraft();
        parser.loadCbsSportsDraft();
        parser.loadDraftTek();
        parser.insertPlayers();

//		JSONService jsonService = new JSONService();
//		jsonService.jsonToExcel("fatracker.json", "/Users/lancefallon/Desktop/FreeAgents2016.xls", "freeAgents", Arrays.asList("videoId", "contentId", "gsisPlayerId", "ex", "th", "id", "rankOrder", "playerId", "lastUpdate", "analysis"));
    }

    private static void testParser() {

        String test = "DOG*’'CAT";
        test = test.replaceAll("\\*’'", "");
        System.out.println(test);

//        String input = "Josh Rosen*, QB, UCLA <br> Height: 6-4. Weight: 210. <br> Projected 40 Time: 4.75. <br> Projected Round (2018): 1.";
//        String[] parts = input.split(",");
//        String name = parts[0].trim().replaceAll("\\W", "");
//        String position = parts[1].trim();
//        String college = parts[2].trim();
//
//        int startIdx = input.indexOf("Height: ") + "Height: ".length();
//        String value = input.substring(startIdx, startIdx + input.substring(startIdx).indexOf("."));
//
//        startIdx = input.indexOf("Weight: ") + "Weight: ".length();
//        value = input.substring(startIdx, startIdx + input.substring(startIdx).indexOf("."));
//
//        startIdx = input.indexOf("Projected 40 Time: ") + "Projected 40 Time: ".length();
//        value = input.substring(startIdx, startIdx + input.substring(startIdx).indexOf("."));
//
//        System.out.println("Pause");
    }

}
