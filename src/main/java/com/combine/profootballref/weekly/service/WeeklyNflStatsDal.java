package com.combine.profootballref.weekly.service;

import java.util.List;

import com.combine.dal.DataSourceLayer;
import com.combine.profootballref.weekly.dao.WeeklyNflStatsDao;
import com.combine.profootballref.weekly.dto.GameScoringPlay;
import com.combine.profootballref.weekly.dto.WeeklyStatsDefense;
import com.combine.profootballref.weekly.dto.WeeklyStatsGame;
import com.combine.profootballref.weekly.dto.WeeklyStatsIndividualPlay;
import com.combine.profootballref.weekly.dto.WeeklyStatsPassing;
import com.combine.profootballref.weekly.dto.WeeklyStatsReceiving;
import com.combine.profootballref.weekly.dto.WeeklyStatsRushing;
import com.combine.profootballref.weekly.model.Game;
import com.combine.profootballref.weekly.model.Team;

public class WeeklyNflStatsDal {

	private WeeklyNflStatsDao weeklyNflStatsDao;
	
	/**
	 * dirty method of checking if year already exists in database, done  by checking the total number of games imported.
	 * obviously this won't work for years that are in progress
	 * @param year
	 * @param seasonType
	 * @return
	 */
	public boolean checkYearExists(int year, String seasonType){
		int games = this.weeklyNflStatsDao.getGameCountByYearAndSeasonType(year, seasonType);
		if(seasonType == ProFootballRefService.SEASON_TYPE_REGULAR){
			return games == 256; 
		} else{
			return games == 11;
		}
	}

	public WeeklyNflStatsDal(DataSourceLayer dataSourceLayer) {
		this.weeklyNflStatsDao = dataSourceLayer.getWeeklyNflStatsDao();
	}

	public int addGames(List<Game> games) {
		int inserted = 0;
		for (Game game : games) {
			inserted += this.weeklyNflStatsDao.insertGame(game);
		}
		return inserted;
	}

	public int addTeams(List<Team> teams) {
		int inserted = 0;
		for (Team team : teams) {
			inserted += this.weeklyNflStatsDao.insertTeam(team);
		}
		return inserted;
	}

	public int addGameStats(List<WeeklyStatsGame> weeklyGameStatsList) {
		int inserted = 0;
		for (WeeklyStatsGame gameStats : weeklyGameStatsList) {
			inserted += this.weeklyNflStatsDao.insertGameStats(gameStats);
		}
		return inserted;
	}

	public int addGameScoringPlays(List<GameScoringPlay> gameScoringPlays) {
		int inserted = 0;
		for (GameScoringPlay gameScoringPlay : gameScoringPlays) {
			inserted += this.weeklyNflStatsDao.insertGameScoringPlay(gameScoringPlay);
		}
		return inserted;
	}

	public int addGamePlays(List<WeeklyStatsIndividualPlay> plays) {
		int inserted = 0;
		for (WeeklyStatsIndividualPlay play : plays) {
			inserted += this.weeklyNflStatsDao.insertGamePlay(play);
		}
		return inserted;
	}

	public int addGameDefense(List<WeeklyStatsDefense> weeklyGameStatsDefense) {
		int inserted = 0;
		for (WeeklyStatsDefense defense : weeklyGameStatsDefense) {
			inserted += this.weeklyNflStatsDao.insertGameDefense(defense);
		}
		return inserted;
	}

	public int addGamePassing(List<WeeklyStatsPassing> weeklyGameStatsPassing) {
		int inserted = 0;
		for (WeeklyStatsPassing passing : weeklyGameStatsPassing) {
			inserted += this.weeklyNflStatsDao.insertGamePassing(passing);
		}
		return inserted;
	}

	public int addGameReceiving(List<WeeklyStatsReceiving> weeklyGameStatsReceiving) {
		int inserted = 0;
		for (WeeklyStatsReceiving receiving : weeklyGameStatsReceiving) {
			inserted += this.weeklyNflStatsDao.insertGameReceiving(receiving);
		}
		return inserted;
	}

	public int addGameRushing(List<WeeklyStatsRushing> weeklyGameStatsRushing) {
		int inserted = 0;
		for (WeeklyStatsRushing rushing : weeklyGameStatsRushing) {
			inserted += this.weeklyNflStatsDao.insertGameRushing(rushing);
		}
		return inserted;
	}

	public List<Team> allTeams() {
		return this.weeklyNflStatsDao.allTeams();
	}

}
