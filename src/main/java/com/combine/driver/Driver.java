package com.combine.driver;

import java.io.IOException;
import java.util.List;

import com.combine.dal.DataSourceLayer;
import com.combine.profootballref.weekly.dto.GameScoringPlay;
import com.combine.profootballref.weekly.dto.WeeklyStatsDefense;
import com.combine.profootballref.weekly.dto.WeeklyStatsGame;
import com.combine.profootballref.weekly.dto.WeeklyStatsIndividualPlay;
import com.combine.profootballref.weekly.dto.WeeklyStatsPassing;
import com.combine.profootballref.weekly.dto.WeeklyStatsReceiving;
import com.combine.profootballref.weekly.dto.WeeklyStatsRushing;
import com.combine.profootballref.weekly.model.Game;
import com.combine.profootballref.weekly.model.Team;
import com.combine.profootballref.weekly.service.DataConversionService;
import com.combine.profootballref.weekly.service.ProFootballRefService;
import com.combine.profootballref.weekly.service.WeeklyNflStatsDal;
import com.combine.service.GenericsService;
import com.combine.service.HttpService;
import com.combine.service.TableMapperService;

public class Driver {

	//helper array storing both season types
	private static final String[] seasonTypes = new String[]{ProFootballRefService.SEASON_TYPE_REGULAR, ProFootballRefService.SEASON_TYPE_PLAYOFFS};
	
	public static void main(String[] args) throws IOException {
		
		//declare / inject services
		DataConversionService dataConversionService = new DataConversionService();
		GenericsService genericsService = new GenericsService();
		HttpService httpService = new HttpService();
		TableMapperService tableMapperService = new TableMapperService(genericsService);
		ProFootballRefService proFootballRefService = new ProFootballRefService(tableMapperService, genericsService, httpService, dataConversionService);
		
		//initialize data layer service (performs initial db migration(s), holds connection objects, and dao refs)
		WeeklyNflStatsDal weeklyNflStatsDal = new WeeklyNflStatsDal(DataSourceLayer.getInstance());
		
		//load all the teams right away (could, if want pull this from the database)
		List<Team> teams = proFootballRefService.loadAllTeams();
		
		
		//loop through each season and get the stats
		for(int i = 2016; i >= 1950; i--){
			
			//just renaming variables here for clarity of what the params actually represent.
			//in this case only one year is being added at a time, so the fromYear and toYear vars are identical
			int fromYear = i, toYear = i;
			
			//grab both regular and post-season
			for(String seasonType : seasonTypes){
				//team stats
				List<WeeklyStatsGame> gameStats = proFootballRefService.loadWeeklyStatsGames(seasonType, teams, fromYear, toYear);
				List<Game> games = proFootballRefService.getUniqueGamesList(gameStats);
				List<WeeklyStatsIndividualPlay> plays = proFootballRefService.getPlayByPlay(games, teams);
				List<GameScoringPlay> gameScoringPlays = proFootballRefService.getScoringSummaries(games, teams);
				
				//individual player stats
				List<WeeklyStatsPassing> passing = proFootballRefService.loadWeeklyStatsPassing(seasonType, teams, fromYear, toYear);
				List<WeeklyStatsRushing> rushing = proFootballRefService.loadWeeklyStatsRushing(seasonType, teams, fromYear, toYear);
				List<WeeklyStatsReceiving> receiving = proFootballRefService.loadWeeklyStatsReceiving(seasonType, teams, fromYear, toYear);
				List<WeeklyStatsDefense> defense = proFootballRefService.loadWeeklyStatsDefense(seasonType, teams, fromYear, toYear);
				
				//db insertions
				weeklyNflStatsDal.addGames(games);
				weeklyNflStatsDal.addGameStats(gameStats);
				weeklyNflStatsDal.addGamePlays(plays);
				weeklyNflStatsDal.addGameScoringPlays(gameScoringPlays);
				
				weeklyNflStatsDal.addGamePassing(passing);
				weeklyNflStatsDal.addGameRushing(rushing);
				weeklyNflStatsDal.addGameReceiving(receiving);
				weeklyNflStatsDal.addGameDefense(defense);
				
				System.out.println(fromYear + " to " + toYear + " season(s) imported successfully");
			}
		}

//		JSONArray jsonArray = new JSONArray(passing);
//		System.out.println(jsonArray.toString());
		
	}
}
