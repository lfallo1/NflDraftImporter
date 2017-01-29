package com.combine.profootballref.weekly.model;

import com.combine.annotations.StatField;

public class Team {

	@StatField("team_name")
	private String teamName;
	@StatField("year_min")
	private Integer fromYear;
	@StatField("year_max")
	private Integer toYear;

	private String teamIdentifier;
	private String teamLink;
	
	public Team(){}

	public Team(String teamIdentifier, String teamName, String teamLink, Integer fromYear, Integer toYear) {
		this.teamName = teamName;
		this.fromYear = fromYear;
		this.toYear = toYear;
		this.teamIdentifier = teamIdentifier;
		this.teamLink = teamLink;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Integer getFromYear() {
		return fromYear;
	}

	public void setFromYear(Integer fromYear) {
		this.fromYear = fromYear;
	}

	public Integer getToYear() {
		return toYear;
	}

	public void setToYear(Integer toYear) {
		this.toYear = toYear;
	}

	public String getTeamIdentifier() {
		return teamIdentifier;
	}

	public void setTeamIdentifier(String teamIdentifier) {
		this.teamIdentifier = teamIdentifier;
	}

	public String getTeamLink() {
		return teamLink;
	}

	public void setTeamLink(String teamLink) {
		this.teamLink = teamLink;
	}

}
