package com.combine.model;

public class WorkoutResult {

	private Integer id;
	private Integer participant;
	private Double result;
	private Integer workout;

	public WorkoutResult() {
	}

	public WorkoutResult(Integer id, Integer participant, Double result, Integer workout) {
		this.id = id;
		this.participant = participant;
		this.result = result;
		this.workout = workout;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParticipant() {
		return participant;
	}

	public void setParticipant(Integer participant) {
		this.participant = participant;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public Integer getWorkout() {
		return workout;
	}

	public void setWorkout(Integer workout) {
		this.workout = workout;
	}

}
