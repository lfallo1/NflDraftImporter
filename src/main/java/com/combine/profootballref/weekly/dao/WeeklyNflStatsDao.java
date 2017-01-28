package com.combine.profootballref.weekly.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.combine.profootballref.weekly.dto.GameScoringPlay;
import com.combine.profootballref.weekly.dto.WeeklyStatsDefense;
import com.combine.profootballref.weekly.dto.WeeklyStatsGame;
import com.combine.profootballref.weekly.dto.WeeklyStatsIndividualPlay;
import com.combine.profootballref.weekly.dto.WeeklyStatsPassing;
import com.combine.profootballref.weekly.dto.WeeklyStatsReceiving;
import com.combine.profootballref.weekly.dto.WeeklyStatsRushing;
import com.combine.profootballref.weekly.model.Game;
import com.combine.profootballref.weekly.model.Team;

public class WeeklyNflStatsDao {
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String INSERT_GAME = "INSERT INTO nfl.game(" +
	"game_identifier, game_link, date, game_number, week, day, year_id, gametime, local_time, league, result, season_type, home_team_identifier, away_team_identifier, overtime) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String INSERT_TEAM = "INSERT INTO nfl.team( " +
	"team_identifier, team_name, team_link, from_year, to_year) " +
	"VALUES (?, ?, ?, ?, ?);";
	
	private static final String INSERT_GAME_STATS = "INSERT INTO nfl.game_team_stats(game_identifier, team_identifier, opp_identifier, fourth_down_pct, fourth_down_success, pass_cmp_perc, pass_rating, rush_yds_per_att, temperature, third_down_pct, third_down_success, yds_per_play_defense, yds_per_play_offense, first_down, first_down_pass, first_down_penalty, first_down_rush, fourth_down_att, " +
	" half_1_score_opp, half_1_score_tgl, half_2_score_opp, half_2_score_tgl, pass_att, pass_cmp, pass_int, pass_sacked, pass_sacked_yds, pass_td, pass_yds, penalties, penalties_opp, team_score, opp_score) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String INSERT_GAME_SCORING_PLAY = "INSERT INTO nfl.game_scoring_play( " +
	"game_identifier, scoring_team, home_team_score, quarter, \"time\", visiting_team_score, description) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?);";
	
	private static final String INSERT_GAME_PLAY = "INSERT INTO nfl.game_play( " +
	"game_identifier, description, yards_gained, play_type, expected_pointsafter, expected_pointsbefore, home_win_probability, down, quarter, score_away, score_home, yards_to_go, location, quarter_time_remaining) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String INSERT_GAME_DEFENSE = "INSERT INTO nfl.game_defense( " +
            "sacks, assists, interceptions, interceptiontouchdowns, interceptionyards, " + 
            "tackles, name, player_identifier, game_identifier, team_identifier, " +
            "team_score, opp_score) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_GAME_PASSING = "INSERT INTO nfl.game_passing( " +
            "adjustedyardsperattempt, completionpercentage, rating, yardsperattempt, " + 
            "attempts, completions, interceptions, sacks, sackyards, touchdowns, " +
            "yards, player_identifier, name, game_identifier, team_identifier, " +
            "team_score, opp_score) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String INSERT_GAME_RECEIVING = "INSERT INTO nfl.game_receiving( " +
            "catchpercentage, yardsperreception, yardspertarget, receptions, " + 
            "targets, touchdowns, yards, name, player_identifier, game_identifier, " +
            "team_identifier, team_score, opp_score) " +
    		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String INSERT_GAME_RUSHING = "INSERT INTO nfl.game_rushing( " +
            "rushingyardsperattempt, rushingattempts, rushingtouchdowns, " + 
            "rushingyards, name, player_identifier, game_identifier, team_identifier, " + 
            "team_score, opp_score) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	
	public WeeklyNflStatsDao(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Integer insertGame(Game game){
		try{
			return this.jdbcTemplate.update(INSERT_GAME, new Object[]{game.getGameIdentifier(), game.getGameLink(), game.getDate(), game.getGameNumber(), game.getWeek(),
					game.getDay(), game.getYear_id(), game.getGametime(), game.getLocal_time(), game.getLeague(), game.getResult(), game.getSeasonType(), game.getHomeTeam().getTeamIdentifier(),
					game.getAwayTeam().getTeamIdentifier(), game.isOvertime()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}

	public int insertTeam(Team team) {
		try{
			return this.jdbcTemplate.update(INSERT_TEAM, new Object[]{team.getTeamIdentifier(), team.getTeamName(), team.getTeamLink(), team.getFromYear(), team.getToYear()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int insertGameStats(WeeklyStatsGame weeklyStatsGame) {
		try{
			return this.jdbcTemplate.update(INSERT_GAME_STATS, new Object[]{weeklyStatsGame.getGameIdentifier(), weeklyStatsGame.getTeamIdentifier(), weeklyStatsGame.getOpponentIdentifier(), weeklyStatsGame.getFourth_down_pct(), weeklyStatsGame.getFourth_down_success(),
					weeklyStatsGame.getPass_cmp_perc(), weeklyStatsGame.getPass_rating(), weeklyStatsGame.getRush_yds_per_att(), weeklyStatsGame.getTemperature(), weeklyStatsGame.getThird_down_pct(), weeklyStatsGame.getThird_down_success(), weeklyStatsGame.getYds_per_play_defense(), weeklyStatsGame.getYds_per_play_offense(), weeklyStatsGame.getFirst_down(), weeklyStatsGame.getFirst_down_pass(), weeklyStatsGame.getFirst_down_penalty(), weeklyStatsGame.getFirst_down_rush(), weeklyStatsGame.getFourth_down_att(),
					weeklyStatsGame.getHalf_1_score_opp(), weeklyStatsGame.getHalf_1_score_tgl(), weeklyStatsGame.getHalf_2_score_opp(), weeklyStatsGame.getHalf_2_score_tgl(), weeklyStatsGame.getPass_att(), weeklyStatsGame.getPass_cmp(), weeklyStatsGame.getPass_int(), weeklyStatsGame.getPass_sacked(), weeklyStatsGame.getPass_sacked_yds(), weeklyStatsGame.getPass_td(), weeklyStatsGame.getPass_yds(), weeklyStatsGame.getPenalties(), weeklyStatsGame.getPenalties_opp(), weeklyStatsGame.getTeamScore(), weeklyStatsGame.getOppScore()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int insertGameScoringPlay(GameScoringPlay gameScoringPlay) {
		try{
			return this.jdbcTemplate.update(INSERT_GAME_SCORING_PLAY, new Object[]{gameScoringPlay.getGameIdentifier(), gameScoringPlay.getTeamObject().getTeamIdentifier(), gameScoringPlay.getHomeTeamScore(), gameScoringPlay.getQuarter(), gameScoringPlay.getTime(), gameScoringPlay.getVisitingTeamScore(), gameScoringPlay.getDescription()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int insertGamePlay(WeeklyStatsIndividualPlay s) {
		try{
			return this.jdbcTemplate.update(INSERT_GAME_PLAY, new Object[]{s.getGameIdentifier(), s.getDescription(), s.getYardsGained(), s.getPlayType().toString(), s.getExpectedPointsAfter(), s.getExpectedPointsBefore(), s.getHomeWinProbability(), s.getDown(), s.getQuarter(), s.getScoreAway(), s.getScoreHome(), s.getYardsToGo(), s.getLocation(), s.getQuarterTimeRemaining()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int insertGameRushing(WeeklyStatsRushing rushing) {
		try{
			return this.jdbcTemplate.update(INSERT_GAME_RUSHING, new Object[]{rushing.getRushingYardsPerAttempt(), rushing.getRushingAttempts(),
					rushing.getRushingTouchdowns(), rushing.getRushingYards(), rushing.getName(), rushing.getPlayerIdentifier(), rushing.getGameIdentifier(),
					rushing.getTeamIdentifier(), rushing.getTeamScore(), rushing.getOppScore()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int insertGameDefense(WeeklyStatsDefense defense) {
		try{
			return this.jdbcTemplate.update(INSERT_GAME_DEFENSE, new Object[]{defense.getSacks(), defense.getAssists(), defense.getInterceptions(),
					defense.getInterceptionTouchdowns(), defense.getInterceptionYards(), defense.getTackles(), defense.getName(), defense.getPlayerIdentifier(),
					defense.getGameIdentifier(), defense.getTeamIdentifier(), defense.getTeamScore(), defense.getOppScore()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int insertGamePassing(WeeklyStatsPassing passing) {
		try{
			return this.jdbcTemplate.update(INSERT_GAME_PASSING, new Object[]{passing.getAdjustedYardsPerAttempt(), passing.getCompletionPercentage(),
					passing.getRating(), passing.getYardsPerAttempt(), passing.getAttempts(), passing.getCompletions(), passing.getInterceptions(),
					passing.getSacks(), passing.getSackYards(), passing.getTouchdowns(), passing.getYards(), passing.getPlayerIdentifier(), passing.getName(),
					passing.getGameIdentifier(), passing.getTeamIdentifier(), passing.getTeamScore(), passing.getOppScore()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public int insertGameReceiving(WeeklyStatsReceiving receiving) {
		try{
			return this.jdbcTemplate.update(INSERT_GAME_RECEIVING, new Object[]{receiving.getCatchPercentage(), receiving.getYardsPerReception(),
					receiving.getYardsPerTarget(), receiving.getReceptions(), receiving.getTargets(), receiving.getTouchdowns(), receiving.getYards(), receiving.getName(),
					receiving.getPlayerIdentifier(), receiving.getGameIdentifier(), receiving.getTeamIdentifier(), receiving.getTeamScore(), receiving.getOppScore()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}
}
