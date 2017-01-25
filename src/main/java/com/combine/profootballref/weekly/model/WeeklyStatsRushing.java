package com.combine.profootballref.weekly.model;

import com.combine.annotations.StatField;

public class WeeklyStatsRushing extends WeeklyStats {

	@StatField("Att")
	private Integer rushingAttempts;
	@StatField("Yds")
	private Integer rushingYards;
	@StatField("Y/A")
	private Double rushingYardsPerAttempt;
	@StatField("TD")
	private Integer rushingTouchdowns;

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
