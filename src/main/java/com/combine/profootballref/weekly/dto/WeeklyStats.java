package com.combine.profootballref.weekly.dto;

import java.util.Date;

import com.combine.annotations.StatField;
import com.combine.profootballref.weekly.model.Team;

public abstract class WeeklyStats {

	private static final String LEAGUE_DEFAULT_NFL = "NFL";

	// models
	private Team teamObject;
	private Team opponentObject;

	// identifier props
	private String gameIdentifier;
	private String gameLink;
	private String playerIdentifier;
	private String playerLink;
	private String seasonType;

	private String teamName;
	private String teamIdentifier;
	private String opponentName;
	private String opponentIdentifier;

	@StatField("game_date")
	private Date date;
	@StatField("league_id")
	private String league;
	@StatField("team")
	private String team;
	@StatField("game_location")
	private String gameLocation;
	@StatField("opp")
	private String opponent;
	@StatField("game_result")
	private String result;
	@StatField("game_num")
	private Integer gameNumber;
	@StatField("week_num")
	private Integer week;
	@StatField("game_day_of_week")
	private String day;

	/**
	 * Constructor
	 */
	public WeeklyStats() {
		this.league = LEAGUE_DEFAULT_NFL;
	}

	public String getPlayerLink() {
		return playerLink;
	}

	public void setPlayerLink(String playerLink) {
		this.playerLink = playerLink;
	}

	public String getPlayerIdentifier() {
		return playerIdentifier;
	}

	public void setPlayerIdentifier(String playerIdentifier) {
		this.playerIdentifier = playerIdentifier;
	}

	public String getGameIdentifier() {
		return gameIdentifier;
	}

	public void setGameIdentifier(String gameIdentifier) {
		this.gameIdentifier = gameIdentifier;
	}

	public String getGameLink() {
		return gameLink;
	}

	public void setGameLink(String gameLink) {
		this.gameLink = gameLink;
	}

	public Team getTeamObject() {
		return teamObject;
	}

	public void setTeamObject(Team teamObject) {
		this.teamObject = teamObject;
	}

	public Team getOpponentObject() {
		return opponentObject;
	}

	public void setOpponentObject(Team opponentObject) {
		this.opponentObject = opponentObject;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getGameLocation() {
		return gameLocation;
	}

	public void setGameLocation(String gameLocation) {
		this.gameLocation = gameLocation;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(Integer gameNumber) {
		this.gameNumber = gameNumber;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamIdentifier() {
		return teamIdentifier;
	}

	public void setTeamIdentifier(String teamIdentifier) {
		this.teamIdentifier = teamIdentifier;
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public String getOpponentIdentifier() {
		return opponentIdentifier;
	}

	public void setOpponentIdentifier(String opponentIdentifier) {
		this.opponentIdentifier = opponentIdentifier;
	}

	public String getSeasonType() {
		return seasonType;
	}

	public void setSeasonType(String seasonType) {
		this.seasonType = seasonType;
	}

}
