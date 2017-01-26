package com.combine.profootballref.weekly.model;

import com.combine.annotations.StatField;

public class WeeklyStatsReceiving extends WeeklyStats {

	@StatField("player")
	private String name;
	@StatField("age")
	private String age;
	@StatField("targets")
	private Integer targets;
	@StatField("rec")
	private Integer receptions;
	@StatField("rec_yds")
	private Integer yards;
	@StatField("rec_yds_per_rec")
	private Double yardsPerReception;
	@StatField("rec_td")
	private Integer touchdowns;
	@StatField("catch_pct")
	private Double catchPercentage;
	@StatField("rec_yds_per_tgt")
	private Double yardsPerTarget;

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

	public Double getCatchPercentage() {
		return catchPercentage;
	}

	public void setCatchPercentage(Double catchPercentage) {
		this.catchPercentage = catchPercentage;
	}

	public Double getYardsPerTarget() {
		return yardsPerTarget;
	}

	public void setYardsPerTarget(Double yardsPerTarget) {
		this.yardsPerTarget = yardsPerTarget;
	}

}
