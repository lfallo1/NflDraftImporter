package com.combine.profootballref.weekly.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.combine.dao.GenericMapper;
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
	"game_identifier, game_link, date, game_number, week, day, year_id, gametime, local_time, league, result, season_type, home_team_identifier, away_team_identifier, home_score, away_score, overtime) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String INSERT_TEAM = "INSERT INTO nfl.team( " +
	"team_identifier, team_name, team_link, from_year, to_year) " +
	"VALUES (?, ?, ?, ?, ?);";
	
	private static final String INSERT_GAME_STATS = "INSERT INTO nfl.game_team_stats( " +
            "game_identifier, team_identifier, opponent_identifier, " +
            "pass_cmp, pass_att, pass_cmp_perc, pass_yds, pass_td,  " +
            "pass_int, pass_sacked, pass_sacked_yds, pass_rating, rush_att,  " +
            "rush_yds, rush_yds_per_att, rush_td, tot_yds, plays_offense,  " +
            "yds_per_play_offense, plays_defense, yds_per_play_defense, turnovers, " + 
            "time_of_poss, duration, penalties, penalties_yds, penalties_opp,  " +
            "penalties_yds_opp, first_down, first_down_rush, first_down_pass,  " +
            "first_down_penalty, third_down_att, third_down_success, third_down_pct, " + 
            "fourth_down_att, fourth_down_success, fourth_down_pct, quarter_1_score_tgl,  " +
            "quarter_2_score_tgl, quarter_3_score_tgl, quarter_4_score_tgl,  " +
            "quarter_1_score_opp, quarter_2_score_opp, quarter_3_score_opp,  " +
            "quarter_4_score_opp, half_1_score_tgl, half_2_score_tgl, half_1_score_opp, " + 
            "half_2_score_opp, surface, roof, temperature, team_score, opponent_score) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	
	public int insertGameStats(WeeklyStatsGame s) {
		try{
			return this.jdbcTemplate.update(INSERT_GAME_STATS, new Object[]{s.getGameIdentifier(), 
					s.getTeamIdentifier(), s.getOpponentIdentifier(), s.getPass_cmp(), s.getPass_att(), s.getPass_cmp_perc(),
					s.getPass_yds(), s.getPass_td(), s.getPass_int(), s.getPass_sacked(), s.getPass_sacked_yds(), s.getPass_rating(),
					s.getRush_att(), s.getRush_yds(), s.getRush_yds_per_att(), s.getRush_td(), s.getTot_yds(), s.getPlays_offense(),
					s.getYds_per_play_offense(), s.getPlays_defense(), s.getYds_per_play_defense(), s.getTurnovers(), s.getTime_of_poss(),
					s.getDuration(), s.getPenalties(), s.getPenalties_yds(), s.getPenalties_opp(), s.getPenalties_yds_opp(), s.getFirst_down(),
					s.getFirst_down_rush(), s.getFirst_down_pass(), s.getFirst_down_penalty(), s.getThird_down_att(), s.getThird_down_success(),
					s.getThird_down_pct(), s.getFourth_down_att(), s.getFourth_down_success(), s.getFourth_down_pct(), s.getQuarter_1_score_tgl(),
					s.getQuarter_2_score_tgl(), s.getQuarter_3_score_tgl(), s.getQuarter_4_score_tgl(), s.getQuarter_1_score_opp(),
					s.getQuarter_2_score_opp(), s.getQuarter_3_score_opp(), s.getQuarter_4_score_opp(), s.getHalf_1_score_tgl(), s.getHalf_2_score_tgl(),
					s.getHalf_1_score_opp(), s.getHalf_2_score_opp(), s.getSurface(), s.getRoof(), s.getTemperature(), s.getTeamScore(), s.getOppScore()});
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	private static final String INSERT_GAME_SCORING_PLAY = "INSERT INTO nfl.game_scoring_play( " +
	"game_identifier, scoring_team, home_team_score, quarter, \"time\", visiting_team_score, description) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?);";
	
	private static final String INSERT_GAME_PLAY = "INSERT INTO nfl.game_play_details( " +
	"game_identifier, description, yards_gained, play_type, expected_pointsafter, expected_pointsbefore, expected_points_difference, down, quarter, team_identifier, opp_identifier, score_team, score_opp, yards_to_go, location, quarter_time_remaining) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
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
					game.getAwayTeam().getTeamIdentifier(), game.getPointsHome(), game.getPointsAway(), game.isOvertime()});
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
			return this.jdbcTemplate.update(INSERT_GAME_PLAY, new Object[]{s.getGameIdentifier(), s.getDescription(), s.getYardsGained(), s.getPlayTypeString(), s.getExpectedPointsAfter(), s.getExpectedPointsBefore(), s.getExpectedPointsDifference(), s.getDown(), s.getQuarter(), s.getTeam(), s.getOpp(), s.getTeamScore(), s.getOppScore(), s.getYardsToGo(), s.getLocationInt(), s.getQuarterTimeRemaining()});
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

	public int getGameCountByYearAndSeasonType(int year, String seasonType) {
		try{
			return this.jdbcTemplate.queryForObject("select count(*) from nfl.games where year = ? and season_type = ?", new Object[]{year, seasonType}, Integer.class);
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return 0;
		}
	}

	public List<Team> allTeams() {
		try{
			return this.jdbcTemplate.query("select * from nfl.team", new GenericMapper<Team>(Team.class));
		} catch(DataAccessException e){
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	public List<Team> allTeams(int fromYear) {
		String sql = "select *, case when from_year < ? then ? else from_year end as start_year from nfl.team where to_year >= ?;";
		return this.jdbcTemplate.query(sql, new Object[]{fromYear,fromYear,fromYear}, new GenericMapper<Team>(Team.class));
	}
}
