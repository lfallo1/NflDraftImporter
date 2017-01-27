package com.combine.profootballref.weekly.dto;

import com.combine.annotations.StatField;

public class WeeklyStatsPassing extends WeeklyStats {

	@StatField("player")
	private String name;
	@StatField("age")
	private String age;
	@StatField("pass_cmp")
	private Integer completions;
	@StatField("pass_att")
	private Integer attempts;
	@StatField("pass_cmp_perc")
	private Double completionPercentage;
	@StatField("pass_yds")
	private Integer yards;
	@StatField("pass_td")
	private Integer touchdowns;
	@StatField("pass_int")
	private Integer interceptions;
	@StatField("pass_rating")
	private Double rating;
	@StatField("pass_sacked")
	private Integer sacks;
	@StatField("pass_sacked_yds")
	private Integer sackYards;
	@StatField("pass_yds_per_att")
	private Double yardsPerAttempt;
	@StatField("pass_adj_yds_per_att")
	private Double adjustedYardsPerAttempt;

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

	public Integer getSackYards() {
		return sackYards;
	}

	public void setSackYards(Integer sackYards) {
		this.sackYards = sackYards;
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
