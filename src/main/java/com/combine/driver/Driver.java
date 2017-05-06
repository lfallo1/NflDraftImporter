package com.combine.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.combine.dal.DataSourceLayer;
import com.combine.profootballref.weekly.dto.WeeklyStatsGame;
import com.combine.profootballref.weekly.dto.WeeklyStatsIndividualPlay;
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
		HttpService httpService = new HttpService();
		DataConversionService dataConversionService = new DataConversionService(httpService);
		GenericsService genericsService = new GenericsService();
		
		TableMapperService tableMapperService = new TableMapperService(genericsService);
		ProFootballRefService proFootballRefService = new ProFootballRefService(tableMapperService, genericsService, httpService, dataConversionService);
		
		//initialize data layer service (performs initial db migration(s), holds connection objects, and dao refs)
		WeeklyNflStatsDal weeklyNflStatsDal = new WeeklyNflStatsDal(DataSourceLayer.getInstance());
		
		//load all the teams first (load from web if not yet in db)
//		List<Team> teams = weeklyNflStatsDal.allTeams(); 
//		if(teams.size() == 0){
//			teams = proFootballRefService.loadAllTeams();
//			weeklyNflStatsDal.addTeams(teams);	
//		}
		
		List<Team> teams = weeklyNflStatsDal.allTeams(1994);
		
		proFootballRefService.loadPlayByPlay(teams, weeklyNflStatsDal);
		
		//execute data import
//		loadStatsBetweenYears(proFootballRefService, weeklyNflStatsDal, teams, 2016, 1950);
	}

	/**
	 * laod all stats by year interval
	 * @param proFootballRefService
	 * @param weeklyNflStatsDal
	 * @param teams
	 * @param from
	 * @param to
	 */
	private static void loadStatsBetweenYears(ProFootballRefService proFootballRefService,
			WeeklyNflStatsDal weeklyNflStatsDal, List<Team> teams, int from, int to) {
		//loop through each season and get the stats
		for(int year = from; year >= to; year--){
			
			System.out.println("starting " + year);
			
			//grab both regular and post-season
			for(String seasonType : seasonTypes){
				//team stats
				List<WeeklyStatsGame> gameStats = proFootballRefService.loadWeeklyStatsGames(seasonType, teams, year);
				List<Game> games = proFootballRefService.getUniqueGamesList(gameStats);
//				List<GameScoringPlay> gameScoringPlays = proFootballRefService.getScoringSummaries(games, teams);
				//load play-by-play for years 1994 & after (data not available prior to this date... at this time)
				List<WeeklyStatsIndividualPlay> plays = year >= 1994 ? proFootballRefService.getPlayByPlay(games, teams) : new ArrayList<>();
				
				
//				individual player stats
//				List<WeeklyStatsPassing> passing = proFootballRefService.loadWeeklyStatsPassing(seasonType, teams, year);
//				List<WeeklyStatsRushing> rushing = proFootballRefService.loadWeeklyStatsRushing(seasonType, teams, year);
//				List<WeeklyStatsReceiving> receiving = proFootballRefService.loadWeeklyStatsReceiving(seasonType, teams, year);
//				List<WeeklyStatsDefense> defense = proFootballRefService.loadWeeklyStatsDefense(seasonType, teams, year);
				
				//db insertions
				int inserted = weeklyNflStatsDal.addGames(games);
				System.out.println("inserted " + inserted + " games");
//				weeklyNflStatsDal.addGameStats(gameStats);
//				weeklyNflStatsDal.addGamePlays(plays);
//				weeklyNflStatsDal.addGameScoringPlays(gameScoringPlays);

//				int inserted = weeklyNflStatsDal.addGamePassing(passing);
//				inserted = weeklyNflStatsDal.addGameRushing(rushing);
//				inserted = weeklyNflStatsDal.addGameReceiving(receiving);
				System.out.println("inserted " + inserted + " games");
//				weeklyNflStatsDal.addGameDefense(defense);
				
//				System.out.println(year + " season (" + seasonType + ") imported successfully");
			}
		}
	}
}
