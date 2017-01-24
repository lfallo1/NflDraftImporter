package com.combine.model.profootballref;

import java.util.Date;

public class WeeklyStatsQuarterback {

	@StatField("Rk")
	private Integer rank;
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
	@StatField("Tgt")
	private Integer targets;
	@StatField("Rec")
	private Integer receptions;
	@StatField("Yds")
	private Integer yards;
	@StatField("Y/R")
	private Double yardsPerReception;
	@StatField("TD")
	private Integer touchdowns;
	@StatField("Ctch%")
	private String catchPercentage;
	@StatField("Y/Tgt")
	private String yardsPerTarget;
	
	public WeeklyStatsQuarterback(){}
	
	public WeeklyStatsQuarterback(Integer rank, String name, String age, Date date, String league, String team,
			String opponent, String result, Integer gameNumber, Integer week, String day, Integer targets,
			Integer receptions, Integer yards, Double yardsPerReception, Integer touchdowns, String catchPercentage,
			String yardsPerTarget) {
		this.rank = rank;
		this.name = name;
		this.age = age;
		this.date = date;
		this.league = league;
		this.team = team;
		this.opponent = opponent;
		this.result = result;
		this.gameNumber = gameNumber;
		this.week = week;
		this.day = day;
		this.targets = targets;
		this.receptions = receptions;
		this.yards = yards;
		this.yardsPerReception = yardsPerReception;
		this.touchdowns = touchdowns;
		this.catchPercentage = catchPercentage;
		this.yardsPerTarget = yardsPerTarget;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
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
	public Integer getTargets() {
		return targets;
	}
	public void setTargets(Integer targets) {
		this.targets = targets;
	}
	public Integer getReceptions() {
		return receptions;
	}
	public void setReceptions(Integer receptions) {
		this.receptions = receptions;
	}
	public Integer getYards() {
		return yards;
	}
	public void setYards(Integer yards) {
		this.yards = yards;
	}
	public Double getYardsPerReception() {
		return yardsPerReception;
	}
	public void setYardsPerReception(Double yardsPerReception) {
		this.yardsPerReception = yardsPerReception;
	}
	public Integer getTouchdowns() {
		return touchdowns;
	}
	public void setTouchdowns(Integer touchdowns) {
		this.touchdowns = touchdowns;
	}
	public String getCatchPercentage() {
		return catchPercentage;
	}
	public void setCatchPercentage(String catchPercentage) {
		this.catchPercentage = catchPercentage;
	}
	public String getYardsPerTarget() {
		return yardsPerTarget;
	}
	public void setYardsPerTarget(String yardsPerTarget) {
		this.yardsPerTarget = yardsPerTarget;
	}
	
	
}
