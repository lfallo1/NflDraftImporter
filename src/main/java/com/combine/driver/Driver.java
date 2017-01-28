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

	public static void main(String[] args) throws IOException {
		//declare / inject services
		DataConversionService dataConversionService = new DataConversionService();
		GenericsService genericsService = new GenericsService();
		HttpService httpService = new HttpService();
		TableMapperService tableMapperService = new TableMapperService(genericsService);
		ProFootballRefService proFootballRefService = new ProFootballRefService(tableMapperService, genericsService, httpService, dataConversionService);
		
		//team and game stats
		List<Team> teams = proFootballRefService.loadAllTeams();
		List<WeeklyStatsGame> gameStats = proFootballRefService.loadWeeklyStatsGames(ProFootballRefService.SEASON_TYPE_PLAYOFFS, teams);
		List<Game> games = proFootballRefService.getUniqueGamesList(gameStats);
//		List<WeeklyStatsIndividualPlay> plays = proFootballRefService.getPlayByPlay(games, teams);
//		List<GameScoringPlay> gameScoringPlays = proFootballRefService.getScoringSummaries(games, teams);
		
		//individual player stats
		List<WeeklyStatsPassing> passing = proFootballRefService.loadWeeklyStatsPassing(ProFootballRefService.SEASON_TYPE_PLAYOFFS, teams);
		List<WeeklyStatsRushing> rushing = proFootballRefService.loadWeeklyStatsRushing(ProFootballRefService.SEASON_TYPE_PLAYOFFS, teams);
		List<WeeklyStatsReceiving> receiving = proFootballRefService.loadWeeklyStatsReceiving(ProFootballRefService.SEASON_TYPE_PLAYOFFS, teams);
		List<WeeklyStatsDefense> defense = proFootballRefService.loadWeeklyStatsDefense(ProFootballRefService.SEASON_TYPE_PLAYOFFS, teams);
		
		//db insertions
		WeeklyNflStatsDal weeklyNflStatsDal = new WeeklyNflStatsDal(DataSourceLayer.getInstance());
		weeklyNflStatsDal.addTeams(teams);
		weeklyNflStatsDal.addGames(games);
		weeklyNflStatsDal.addGameStats(gameStats);
//		weeklyNflStatsDal.insertGamePlays(plays);
//		weeklyNflStatsDal.insertGameScoringPlays(gameScoringPlays);
		
		weeklyNflStatsDal.addGamePassing(passing);
		weeklyNflStatsDal.addGameRushing(rushing);
		weeklyNflStatsDal.addGameReceiving(receiving);
		weeklyNflStatsDal.addGameDefense(defense);
		

		System.out.println("pause...");
//		JSONArray jsonArray = new JSONArray(passing);
//		System.out.println(jsonArray.toString());
		
	}
}
