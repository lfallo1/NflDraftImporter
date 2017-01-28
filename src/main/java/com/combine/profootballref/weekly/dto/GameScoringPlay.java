package com.combine.profootballref.weekly.dto;

import com.combine.annotations.StatField;

public class GameScoringPlay extends WeeklyStats {

	@StatField("quarter")
	private Integer quarter;
	@StatField("time")
	private String time;
	@StatField("team")
	private String scoringTeamName;
	@StatField("description")
	private String description;
	@StatField("vis_team_score")
	private Integer visitingTeamScore;
	@StatField("home_team_score")
	private Integer homeTeamScore;

	public Integer getQuarter() {
		return quarter;
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getScoringTeamName() {
		return scoringTeamName;
	}

	public void setScoringTeamName(String scoringTeamName) {
		this.scoringTeamName = scoringTeamName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getVisitingTeamScore() {
		return visitingTeamScore;
	}

	public void setVisitingTeamScore(Integer visitingTeamScore) {
		this.visitingTeamScore = visitingTeamScore;
	}

	public Integer getHomeTeamScore() {
		return homeTeamScore;
	}

	public void setHomeTeamScore(Integer homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}

}