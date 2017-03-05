package com.combine.model;

public class Player {

	private Integer id;
	private Integer rank;
	private String name;
	private Integer college;
	private String collegeText;
	private String position;
	private Double height;
	private Double weight;
	private Integer positionRank;
	private String projectedRound;
	private String yearClass;
	private Integer year;
	private String importUUID;

	// workout properties
	private Double fortyYardDash;
	private Double benchPress;
	private Double verticalJump;
	private Double broadJump;
	private Double threeConeDrill;
	private Double twentyYardShuttle;
	private Double sixtyYardShuttle;
	private Double handSize;
	private Double armLength;

	public Player() {
		this.rank = 0;
	}

	public Player(Integer rank, String name, Integer college, String collegeText, String position, Double height,
			Double weight, Integer positionRank, String projectedRound, String yearClass, Integer year,
			String importUUID) {
		this.rank = rank;
		this.name = name;
		this.college = college;
		this.collegeText = collegeText;
		this.position = position;
		this.height = height;
		this.weight = weight;
		this.positionRank = positionRank;
		this.projectedRound = projectedRound;
		this.yearClass = yearClass;
		this.year = year;
		this.importUUID = importUUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getCollege() {
		return college;
	}

	public void setCollege(Integer college) {
		this.college = college;
	}

	public String getCollegeText() {
		return collegeText;
	}

	public void setCollegeText(String collegeText) {
		this.collegeText = collegeText;
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

	public String getImportUUID() {
		return importUUID;
	}

	public void setImportUUID(String importUUID) {
		this.importUUID = importUUID;
	}

	public Double getFortyYardDash() {
		return fortyYardDash;
	}

	public void setFortyYardDash(Double fortyYardDash) {
		this.fortyYardDash = fortyYardDash;
	}

	public Double getBenchPress() {
		return benchPress;
	}

	public void setBenchPress(Double benchPress) {
		this.benchPress = benchPress;
	}

	public Double getVerticalJump() {
		return verticalJump;
	}

	public void setVerticalJump(Double verticalJump) {
		this.verticalJump = verticalJump;
	}

	public Double getBroadJump() {
		return broadJump;
	}

	public void setBroadJump(Double broadJump) {
		this.broadJump = broadJump;
	}

	public Double getThreeConeDrill() {
		return threeConeDrill;
	}

	public void setThreeConeDrill(Double threeConeDrill) {
		this.threeConeDrill = threeConeDrill;
	}

	public Double getTwentyYardShuttle() {
		return twentyYardShuttle;
	}

	public void setTwentyYardShuttle(Double twentyYardShuttle) {
		this.twentyYardShuttle = twentyYardShuttle;
	}

	public Double getSixtyYardShuttle() {
		return sixtyYardShuttle;
	}

	public void setSixtyYardShuttle(Double sixtyYardShuttle) {
		this.sixtyYardShuttle = sixtyYardShuttle;
	}

	public Double getHandSize() {
		return handSize;
	}

	public void setHandSize(Double handSize) {
		this.handSize = handSize;
	}

	public Double getArmLength() {
		return armLength;
	}

	public void setArmLength(Double armLength) {
		this.armLength = armLength;
	}

}
