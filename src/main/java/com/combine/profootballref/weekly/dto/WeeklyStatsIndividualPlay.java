package com.combine.profootballref.weekly.dto;

import com.combine.annotations.StatField;

public class WeeklyStatsIndividualPlay {

	/**
	 * {0=game_date, 1=team, 2=opp, 3=quarter, 4=qtr_time_remain, 5=down,
	 * 6=yds_to_go, 7=location, 8=score, 9=description, 10=yards,
	 * 11=exp_pts_before, 12=exp_pts_after, 13=exp_pts_diff}
	 */

	@StatField("description")
	private String description;
	@StatField("team")
	private String team;
	@StatField("opp")
	private String opp;
	@StatField("quarter")
	private Integer quarter;
	@StatField("qtr_time_remain")
	private String quarterTimeRemaining;
	@StatField("down")
	private Integer down;
	@StatField("yds_to_go")
	private Integer yardsToGo;
	@StatField("location")
	private String location;
	@StatField("score")
	private String score;
	@StatField("exp_pts_before")
	private Double expectedPointsBefore;
	@StatField("exp_pts_after")
	private Double expectedPointsAfter;
	@StatField("exp_pts_diff")
	private Double expectedPointsDifference;
	@StatField("yards")
	private Integer yardsGained;

	// individual props
	private String gameIdentifier;
	private PlayType playType;
	private String playTypeString;
	private int teamScore;
	private int oppScore;
	private int locationInt;

	public Integer getQuarter() {
		return quarter;
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public String getQuarterTimeRemaining() {
		return quarterTimeRemaining;
	}

	public void setQuarterTimeRemaining(String quarterTimeRemaining) {
		this.quarterTimeRemaining = quarterTimeRemaining;
	}

	public Integer getDown() {
		return down;
	}

	public void setDown(Integer down) {
		this.down = down;
	}

	public Integer getYardsToGo() {
		return yardsToGo;
	}

	public void setYardsToGo(Integer yardsToGo) {
		this.yardsToGo = yardsToGo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getExpectedPointsBefore() {
		return expectedPointsBefore;
	}

	public void setExpectedPointsBefore(Double expectedPointsBefore) {
		this.expectedPointsBefore = expectedPointsBefore;
	}

	public Double getExpectedPointsAfter() {
		return expectedPointsAfter;
	}

	public void setExpectedPointsAfter(Double expectedPointsAfter) {
		this.expectedPointsAfter = expectedPointsAfter;
	}

	public Double getExpectedPointsDifference() {
		return expectedPointsDifference;
	}

	public void setExpectedPointsDifference(Double expectedPointsDifference) {
		this.expectedPointsDifference = expectedPointsDifference;
	}

	public String getGameIdentifier() {
		return gameIdentifier;
	}

	public void setGameIdentifier(String gameIdentifier) {
		this.gameIdentifier = gameIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getYardsGained() {
		return yardsGained;
	}

	public void setYardsGained(Integer yards) {
		this.yardsGained = yards == null ? 0 : yards;
	}

	public PlayType getPlayType() {
		return playType;
	}

	public void setPlayType(PlayType playType) {
		this.playType = playType;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getPlayTypeString() {
		return playTypeString;
	}

	public void setPlayTypeString(String playTypeString) {
		this.playTypeString = playTypeString;
	}

	public String getOpp() {
		return opp;
	}

	public void setOpp(String opp) {
		this.opp = opp;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public int getTeamScore() {
		return teamScore;
	}

	public void setTeamScore(int teamScore) {
		this.teamScore = teamScore;
	}

	public int getOppScore() {
		return oppScore;
	}

	public void setOppScore(int oppScore) {
		this.oppScore = oppScore;
	}

	public int getLocationInt() {
		return locationInt;
	}

	public void setLocationInt(int locationInt) {
		this.locationInt = locationInt;
	}

}
