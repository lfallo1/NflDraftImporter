package com.combine.profootballref.weekly.dto;

import com.combine.annotations.StatField;

public class WeeklyStatsDefense extends WeeklyStats {

	@StatField("player")
	private String name;
	@StatField("age")
	private String age;
	@StatField("sacks")
	private Double sacks;
	@StatField("tackles_solo")
	private Integer tackles;
	@StatField("tackles_assists")
	private Integer assists;
	@StatField("def_int")
	private Integer interceptions;
	@StatField("def_int_yds")
	private Integer interceptionYards;
	@StatField("def_int_td")
	private Integer interceptionTouchdowns;

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
	
	public Double getSacks() {
		return sacks;
	}

	public void setSacks(Double sacks) {
		this.sacks = sacks;
	}

	public Integer getTackles() {
		return tackles;
	}

	public void setTackles(Integer tackles) {
		this.tackles = tackles;
	}

	public Integer getAssists() {
		return assists;
	}

	public void setAssists(Integer assists) {
		this.assists = assists;
	}

	public Integer getInterceptions() {
		return interceptions;
	}

	public void setInterceptions(Integer interceptions) {
		this.interceptions = interceptions;
	}

	public Integer getInterceptionYards() {
		return interceptionYards;
	}

	public void setInterceptionYards(Integer interceptionYards) {
		this.interceptionYards = interceptionYards;
	}

	public Integer getInterceptionTouchdowns() {
		return interceptionTouchdowns;
	}

	public void setInterceptionTouchdowns(Integer interceptionTouchdowns) {
		this.interceptionTouchdowns = interceptionTouchdowns;
	}

}
