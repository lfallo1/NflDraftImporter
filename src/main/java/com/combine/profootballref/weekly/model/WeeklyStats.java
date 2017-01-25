package com.combine.profootballref.weekly.model;

import java.util.Date;

import com.combine.annotations.StatField;

public abstract class WeeklyStats {

	@StatField("Name")
	private String name;
	@StatField("Age")
	private String age;
	@StatField("Date")
	private Date date;
	@StatField("Lg")
	private String league;
	@StatField("Tm")
	private String team;
	@StatField("Opp")
	private String opponent;
	@StatField("Result")
	private String result;
	@StatField("G#")
	private Integer gameNumber;
	@StatField("Week")
	private Integer week;
	@StatField("Day")
	private String day;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
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
}
