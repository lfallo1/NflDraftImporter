package com.combine.profootballref.weekly.model;

import com.combine.annotations.StatField;

public class WeeklyStatsReceiving extends WeeklyStats {

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
	private Double catchPercentage;
	@StatField("Y/Tgt")
	private Double yardsPerTarget;

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
