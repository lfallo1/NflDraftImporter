package com.combine.profootballref.weekly.model;

import java.util.Date;

public class Game {

	private String gameIdentifier;
	private String gameLink;
	private Date date;
	private Integer gameNumber;
	private Integer week;
	private String day;
	private Integer year_id;
	private String gametime;
	private String local_time;
	private String league;
	private String result;
	private String seasonType;
	private Team homeTeam;
	private Team awayTeam;
	private Integer pointsHome;
	private Integer pointsAway;
	private boolean overtime;

	public Game() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public Integer getYear_id() {
		return year_id;
	}

	public void setYear_id(Integer year_id) {
		this.year_id = year_id;
	}

	public String getGametime() {
		return gametime;
	}

	public void setGametime(String gametime) {
		this.gametime = gametime;
	}

	public String getLocal_time() {
		return local_time;
	}

	public void setLocal_time(String local_time) {
		this.local_time = local_time;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

	public String getSeasonType() {
		return seasonType;
	}

	public void setSeasonType(String seasonType) {
		this.seasonType = seasonType;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Integer getPointsHome() {
		return pointsHome;
	}

	public void setPointsHome(Integer pointsHome) {
		this.pointsHome = pointsHome;
	}

	public Integer getPointsAway() {
		return pointsAway;
	}

	public void setPointsAway(Integer pointsAway) {
		this.pointsAway = pointsAway;
	}

	public boolean isOvertime() {
		return overtime;
	}

	public void setOvertime(boolean overtime) {
		this.overtime = overtime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameIdentifier == null) ? 0 : gameIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (gameIdentifier == null) {
			if (other.gameIdentifier != null)
				return false;
		} else if (!gameIdentifier.equals(other.gameIdentifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Game [gameIdentifier=" + gameIdentifier + ", gameLink=" + gameLink + ", date=" + date + ", gameNumber="
				+ gameNumber + ", week=" + week + ", day=" + day + ", year_id=" + year_id + ", gametime=" + gametime
				+ ", local_time=" + local_time + ", league=" + league + ", result=" + result + ", seasonType="
				+ seasonType + ", team=" + homeTeam.getTeamName() + ", opponent=" + awayTeam.getTeamName() + "]";
	}

}
