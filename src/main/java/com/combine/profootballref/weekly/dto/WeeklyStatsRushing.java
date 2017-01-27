package com.combine.profootballref.weekly.dto;

import com.combine.annotations.StatField;

public class WeeklyStatsRushing extends WeeklyStats {

	@StatField("player")
	private String name;
	@StatField("age")
	private String age;
	@StatField("rush_att")
	private Integer rushingAttempts;
	@StatField("rush_yds")
	private Integer rushingYards;
	@StatField("rush_yds_per_att")
	private Double rushingYardsPerAttempt;
	@StatField("rush_td")
	private Integer rushingTouchdowns;

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
	
	public Integer getRushingAttempts() {
		return rushingAttempts;
	}

	public void setRushingAttempts(Integer rushingAttempts) {
		this.rushingAttempts = rushingAttempts;
	}

	public Integer getRushingYards() {
		return rushingYards;
	}

	public void setRushingYards(Integer rushingYards) {
		this.rushingYards = rushingYards;
	}

	public Double getRushingYardsPerAttempt() {
		return rushingYardsPerAttempt;
	}

	public void setRushingYardsPerAttempt(Double rushingYardsPerAttempt) {
		this.rushingYardsPerAttempt = rushingYardsPerAttempt;
	}

	public Integer getRushingTouchdowns() {
		return rushingTouchdowns;
	}

	public void setRushingTouchdowns(Integer rushingTouchdowns) {
		this.rushingTouchdowns = rushingTouchdowns;
	}
}
