package com.combine.profootballref.weekly.service;

import java.util.List;

import com.combine.dal.DataSourceLayer;
import com.combine.profootballref.weekly.dao.WeeklyNflStatsDao;
import com.combine.profootballref.weekly.dto.GameScoringPlay;
import com.combine.profootballref.weekly.dto.WeeklyStatsGame;
import com.combine.profootballref.weekly.dto.WeeklyStatsIndividualPlay;
import com.combine.profootballref.weekly.model.Game;
import com.combine.profootballref.weekly.model.Team;

public class WeeklyNflStatsDal {

	private WeeklyNflStatsDao weeklyNflStatsDao;
	
	public WeeklyNflStatsDal(DataSourceLayer dataSourceLayer){
		this.weeklyNflStatsDao = dataSourceLayer.getWeeklyNflStatsDao();
	}
	
	public int addGames(List<Game> games){
		int inserted = 0;
		for(Game game : games){
			inserted += this.weeklyNflStatsDao.insertGame(game);
		}
		return inserted;
	}
	
	public int addTeams(List<Team> teams){
		int inserted = 0;
		for(Team team : teams){
			inserted += this.weeklyNflStatsDao.insertTeam(team);
		}
		return inserted;
	}
	
	public int addGameStats(List<WeeklyStatsGame> weeklyGameStatsList){
		int inserted = 0;
		for(WeeklyStatsGame gameStats : weeklyGameStatsList){
			inserted += this.weeklyNflStatsDao.insertGameStats(gameStats);
		}
		return inserted;
	}
	
	public int insertGameScoringPlays(List<GameScoringPlay> gameScoringPlays){
		int inserted = 0;
		for(GameScoringPlay gameScoringPlay : gameScoringPlays){
			inserted += this.weeklyNflStatsDao.insertGameScoringPlay(gameScoringPlay);
		}
		return inserted;
	}
	
	public int insertGamePlays(List<WeeklyStatsIndividualPlay> plays){
		int inserted = 0;
		for(WeeklyStatsIndividualPlay play : plays){
			inserted += this.weeklyNflStatsDao.insertGamePlay(play);
		}
		return inserted;
	}
	
}
