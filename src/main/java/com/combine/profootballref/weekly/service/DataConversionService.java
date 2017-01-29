package com.combine.profootballref.weekly.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import com.combine.profootballref.weekly.dto.WeeklyStatsGame;
import com.combine.profootballref.weekly.model.Game;
import com.combine.profootballref.weekly.model.Team;
import com.combine.service.HttpService;

/**
 * help with converting dtos to more db compatible objects
 * @author lancefallon
 *
 */
public class DataConversionService {
	
	private HttpService httpService;
	
	public DataConversionService(HttpService httpService){
		this.httpService = httpService;
	}
	
	public Team findByTeamIdentifier(String teamIdentifier, List<Team> teams){
		return teams.stream().filter(t->t.getTeamIdentifier().toLowerCase().equals(teamIdentifier.toLowerCase())).findFirst().get();
	}
	
	public Team findByTeamName(String teamName, List<Team> teams){
		try{
			return teams.stream().filter(t->t.getTeamName().toLowerCase().indexOf(teamName.toLowerCase()) > -1).findFirst().get();
		} catch(NoSuchElementException e){
			System.out.println("unable to find team: " + teamName);
			return new Team();
		}
	}
	
	private void setHomeAwayTeams(Game game, Team homeTeam, Team awayTeam, Integer homeScore, Integer awayScore){
		game.setHomeTeam(homeTeam);
		game.setAwayTeam(awayTeam);
		game.setPointsHome(homeScore);
		game.setPointsAway(awayScore);
	}
	
	public Set<Game> getUniqueListGames(List<WeeklyStatsGame> gameDtos, String baseUrl){
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
			
//			String homeTeamIdentifier = gameDto.getGameIdentifier().substring(gameDto.getGameIdentifier().length()-3);
			if(StringUtils.isEmpty(gameDto.getGameLocation())){
				setHomeAwayTeams(game, gameDto.getTeamObject(), gameDto.getOpponentObject(), gameDto.getTeamScore(), gameDto.getOppScore());
			} else if(gameDto.getGameLocation().equals("@")){
				setHomeAwayTeams(game, gameDto.getOpponentObject(), gameDto.getTeamObject(), gameDto.getOppScore(), gameDto.getTeamScore());
			} else{
				//its a neutral site then home / away cannot be inferred. need to use the game link and get it from the game page itself
				String url = baseUrl + gameDto.getGameLink();
				try {
					String homeTeamSelector = "#content > div.scorebox > div:nth-child(1) > div:nth-child(1) > strong > a";
					Document doc = httpService.getDocumentFromUrl(url);
					String homeIdentifier = doc.select(homeTeamSelector).attr("href").replace("/teams/", "").substring(0,3);
					if(homeIdentifier.equals(gameDto.getTeamIdentifier())){
						setHomeAwayTeams(game, gameDto.getTeamObject(), gameDto.getOpponentObject(), gameDto.getTeamScore(), gameDto.getOppScore());				
					} else{
						setHomeAwayTeams(game, gameDto.getOpponentObject(), gameDto.getTeamObject(), gameDto.getOppScore(), gameDto.getTeamScore());
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			game.setOvertime(gameDto.getOvertime() != null);
			game.setGameIdentifier(gameDto.getGameIdentifier());
			games.add(game);
		}
		
		return games;
	}

}
