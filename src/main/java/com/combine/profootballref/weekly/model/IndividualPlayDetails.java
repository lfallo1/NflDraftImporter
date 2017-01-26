package com.combine.profootballref.weekly.model;

public class IndividualPlayDetails {

	private String description;
	private Integer yards;
	private boolean isScoringPlay;
	private PlayType playType;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getYards() {
		return yards;
	}

	public void setYards(Integer yards) {
		this.yards = yards;
	}

	public boolean isScoringPlay() {
		return isScoringPlay;
	}

	public void setScoringPlay(boolean isScoringPlay) {
		this.isScoringPlay = isScoringPlay;
	}

	public PlayType getPlayType() {
		return playType;
	}

	public void setPlayType(PlayType playType) {
		this.playType = playType;
	}

}
