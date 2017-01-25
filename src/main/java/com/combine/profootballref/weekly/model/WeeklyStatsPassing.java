package com.combine.profootballref.weekly.model;

import com.combine.annotations.StatField;

public class WeeklyStatsPassing extends WeeklyStats {

	@StatField("Cmp")
	private Integer completions;
	@StatField("Att")
	private Integer attempts;
	@StatField("Cmp%")
	private Double completionPercentage;
	@StatField("Yds")
	private Integer yards;
	@StatField("TD")
	private Integer touchdowns;
	@StatField("Int")
	private Integer interceptions;
	@StatField("Rate")
	private Double rating;
	@StatField("Sk")
	private Integer sacks;
	@StatField("Y/A")
	private Double yardsPerAttempt;
	@StatField("AY/A")
	private Double adjustedYardsPerAttempt;

	public Integer getCompletions() {
		return completions;
	}

	public void setCompletions(Integer completions) {
		this.completions = completions;
	}

	public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public Double getCompletionPercentage() {
		return completionPercentage;
	}

	public void setCompletionPercentage(Double completionPercentage) {
		this.completionPercentage = completionPercentage;
	}

	public Integer getYards() {
		return yards;
	}

	public void setYards(Integer yards) {
		this.yards = yards;
	}

	public Integer getTouchdowns() {
		return touchdowns;
	}

	public void setTouchdowns(Integer touchdowns) {
		this.touchdowns = touchdowns;
	}

	public Integer getInterceptions() {
		return interceptions;
	}

	public void setInterceptions(Integer interceptions) {
		this.interceptions = interceptions;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Integer getSacks() {
		return sacks;
	}

	public void setSacks(Integer sacks) {
		this.sacks = sacks;
	}

	public Double getYardsPerAttempt() {
		return yardsPerAttempt;
	}

	public void setYardsPerAttempt(Double yardsPerAttempt) {
		this.yardsPerAttempt = yardsPerAttempt;
	}

	public Double getAdjustedYardsPerAttempt() {
		return adjustedYardsPerAttempt;
	}

	public void setAdjustedYardsPerAttempt(Double adjustedYardsPerAttempt) {
		this.adjustedYardsPerAttempt = adjustedYardsPerAttempt;
	}

}
