package com.combine.profootballref.weekly.model;

import com.combine.annotations.StatField;

public class WeeklyStatsDefense extends WeeklyStats {
	
	@StatField("Sk")
	private Double sacks;
	@StatField("Tkl")
	private Integer tackles;
	@StatField("Ast")
	private Integer assists;
	@StatField("Int")
	private Integer interceptions;
	@StatField("Yds")
	private Integer interceptionYards;
	@StatField("TD")
	private Integer interceptionTouchdowns;
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
