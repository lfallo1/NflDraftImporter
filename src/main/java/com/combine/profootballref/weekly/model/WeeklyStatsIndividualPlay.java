package com.combine.profootballref.weekly.model;

import com.combine.annotations.StatField;

public class WeeklyStatsIndividualPlay {

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
	@StatField("pbp_score_hm")
	private Integer scoreHome;
	@StatField("pbp_score_aw")
	private Integer scoreAway;
	@StatField("exp_pts_before")
	private Double expectedPointsBefore;
	@StatField("exp_pts_after")
	private Double expectedPointsAfter;
	@StatField("home_wp")
	private Double homeWinProbability;

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

	public Integer getScoreHome() {
		return scoreHome;
	}

	public void setScoreHome(Integer scoreHome) {
		this.scoreHome = scoreHome;
	}

	public Integer getScoreAway() {
		return scoreAway;
	}

	public void setScoreAway(Integer scoreAway) {
		this.scoreAway = scoreAway;
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

	public Double getHomeWinProbability() {
		return homeWinProbability;
	}

	public void setHomeWinProbability(Double homeWinProbability) {
		this.homeWinProbability = homeWinProbability;
	}

}
