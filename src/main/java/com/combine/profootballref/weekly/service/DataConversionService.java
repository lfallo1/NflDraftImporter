package com.combine.profootballref.weekly.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.combine.profootballref.weekly.dto.WeeklyStatsGame;
import com.combine.profootballref.weekly.model.Game;
import com.combine.profootballref.weekly.model.Team;

/**
 * help with converting dtos to more db compatible objects
 * @author lancefallon
 *
 */
public class DataConversionService {
	
	public Team findByTeamIdentifier(String teamIdentifier, List<Team> teams){
		return teams.stream().filter(t->t.getTeamIdentifier().toLowerCase().equals(teamIdentifier.toLowerCase())).findFirst().get();
	}
	
	public Team findByTeamName(String teamName, List<Team> teams){
		return teams.stream().filter(t->t.getTeamName().toLowerCase().indexOf(teamName.toLowerCase()) > -1).findFirst().get();
	}
	
	public Set<Game> getUniqueListGames(List<WeeklyStatsGame> gameDtos){
		Set<Game> games = new HashSet<>();
		for(WeeklyStatsGame gameDto : gameDtos){
			Game game = new Game();
			game.setGameLink(gameDto.getGameLink());
			game.setDate(gameDto.getDate());
			game.setDay(gameDto.getDay());
			game.setGameNumber(gameDto.getGameNumber());
			game.setLeague(gameDto.getLeague());
			game.setLocal_time(gameDto.getLocal_time());
			game.setSeasonType(gameDto.getSeasonType());
			game.setWeek(gameDto.getWeek());
			game.setYear_id(gameDto.getYear_id());
			
			String homeTeamIdentifier = gameDto.getGameIdentifier().substring(gameDto.getGameIdentifier().length()-3);
			if(homeTeamIdentifier.equals(gameDto.getTeamIdentifier())){
				game.setHomeTeam(gameDto.getTeamObject());
				game.setAwayTeam(gameDto.getOpponentObject());
				game.setPointsHome(gameDto.getTeamScore());
				game.setPointsAway(gameDto.getOppScore());
			} else{
				game.setHomeTeam(gameDto.getOpponentObject());
				game.setAwayTeam(gameDto.getTeamObject());
				game.setPointsHome(gameDto.getOppScore());
				game.setPointsAway(gameDto.getTeamScore());
			}
			
			game.setOvertime(gameDto.getOvertime() != null);
			game.setGameIdentifier(gameDto.getGameIdentifier());
			games.add(game);
		}
		
		return games;
	}

}
