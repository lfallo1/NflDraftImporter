package com.combine.model;

public class Player {

	private Integer rank;
	private String name;
	private String college;
	private String position;
	private Double height;
	private Double weight;
	private Integer positionRank;
	private String projectedRound;
	private String yearClass;
	private Integer year;

	public Player() {
		this.rank = 0;
	}

	public Player(Integer rank, String name, String college, String position,
			Double height, Double weight, Integer positionRank,
			String projectedRound, String yearClass, Integer year) {
		this.rank = rank;
		this.name = name;
		this.college = college;
		this.position = position;
		this.height = height;
		this.weight = weight;
		this.positionRank = positionRank;
		this.projectedRound = projectedRound;
		this.yearClass = yearClass;
		this.year = year;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getPositionRank() {
		return positionRank;
	}

	public void setPositionRank(Integer positionRank) {
		this.positionRank = positionRank;
	}

	public String getProjectedRound() {
		return projectedRound;
	}

	public void setProjectedRound(String projectedRound) {
		this.projectedRound = projectedRound;
	}

	public String getYearClass() {
		return yearClass;
	}

	public void setYearClass(String yearClass) {
		this.yearClass = yearClass;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
