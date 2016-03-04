package com.combine.model;

public class Participant {
	private Integer id;
	private String firstname;
	private String lastname;
	private Integer position;
	private Integer college;
	private Double height;
	private Integer weight;
	private Double hands;
	private Double armLength;
	private String overview;
	private String strengths;
	private String weaknesses;
	private String comparision;
	private String bottom_line;
	private String whatScoutsSay;
	private Double expertGrade;
	private String link;

	public Participant() {
	}

	public Participant(Integer id, String firstname, String lastname, Integer position, Double height,
			Integer weight, Double hands, Double armLength, String overview, String strengths, String weaknesses,
			String comparision, String bottom_line, String whatScoutsSay, Double expertGrade, Integer college, String link) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.position = position;
		this.college = college;
		this.height = height;
		this.weight = weight;
		this.hands = hands;
		this.armLength = armLength;
		this.overview = overview;
		this.strengths = strengths;
		this.weaknesses = weaknesses;
		this.comparision = comparision;
		this.bottom_line = bottom_line;
		this.whatScoutsSay = whatScoutsSay;
		this.expertGrade = expertGrade;
		this.link = link;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getCollege() {
		return college;
	}

	public void setCollege(Integer college) {
		this.college = college;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Double getHands() {
		return hands;
	}

	public void setHands(Double hands) {
		this.hands = hands;
	}

	public Double getArmLength() {
		return armLength;
	}

	public void setArmLength(Double armLength) {
		this.armLength = armLength;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getStrengths() {
		return strengths;
	}

	public void setStrengths(String strengths) {
		this.strengths = strengths;
	}

	public String getWeaknesses() {
		return weaknesses;
	}

	public void setWeaknesses(String weaknesses) {
		this.weaknesses = weaknesses;
	}

	public String getComparision() {
		return comparision;
	}

	public void setComparision(String comparision) {
		this.comparision = comparision;
	}

	public String getBottom_line() {
		return bottom_line;
	}

	public void setBottom_line(String bottom_line) {
		this.bottom_line = bottom_line;
	}

	public String getWhatScoutsSay() {
		return whatScoutsSay;
	}

	public void setWhatScoutsSay(String whatScoutsSay) {
		this.whatScoutsSay = whatScoutsSay;
	}

	public Double getExpertGrade() {
		return expertGrade;
	}

	public void setExpertGrade(Double expertGrade) {
		this.expertGrade = expertGrade;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
