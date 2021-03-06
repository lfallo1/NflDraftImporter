package com.combine.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.combine.model.College;
import com.combine.model.Conference;
import com.combine.model.Participant;
import com.combine.model.Position;
import com.combine.model.Workout;
import com.combine.model.WorkoutResult;

public class CombineDao {

	private static final String INSERT_PARTICIPANT = "INSERT INTO participant( id, firstname, lastname, position, height, weight, hands, overview, strengths, weaknesses, comparision, bottom_line, what_scouts_say, college, expert_grade, link, pick) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String INSERT_WORKOUTRESULT = "INSERT INTO workout_result(participant, result, workout) VALUES (?, ?, ?);";
	private static final String INSERT_CONFERENCE = "INSERT INTO conf (id, name) VALUES (?,?)";
	private static final String INSERT_COLLEGE = "INSERT INTO college (id, conf, name) VALUES (?,?,?)";
	private JdbcTemplate jdbcTemplate;
	
	public CombineDao(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Position> getPositions() {
		return this.jdbcTemplate.query("select * from position", new GenericMapper<Position>(Position.class));
	}
	
	public List<Workout> getWorkouts() {
		return this.jdbcTemplate.query("select * from workout", new GenericMapper<Workout>(Workout.class));
	}
	
	public void deleteWorkoutResult() {
		this.jdbcTemplate.update("delete from workout_result");
	}
	
	public void deleteParticipant() {
		this.jdbcTemplate.update("delete from participant");
	}

	public void insertParticipant(Participant participant) {
		this.jdbcTemplate.update(INSERT_PARTICIPANT, new Object[]{participant.getId(), participant.getFirstname(), participant.getLastname(),
				participant.getPosition(), participant.getHeight(), participant.getWeight(), participant.getHands(),
				participant.getOverview(), participant.getStrengths(), participant.getWeaknesses(), participant.getComparision(), 
				participant.getBottom_line(), participant.getWhatScoutsSay(), participant.getCollege(),
				participant.getExpertGrade(), participant.getLink(), participant.getPick()});
	}

	public void insertWorkoutResult(WorkoutResult workoutResult) {
		this.jdbcTemplate.update(INSERT_WORKOUTRESULT, new Object[]{workoutResult.getParticipant(),
				workoutResult.getResult(), workoutResult.getWorkout()});
	}

	public void insertConference(Conference conference) {
		this.jdbcTemplate.update(INSERT_CONFERENCE, new Object[]{conference.getId(),
				conference.getName()});
	}

	public void insertCollege(College college) {
		this.jdbcTemplate.update(INSERT_COLLEGE, new Object[]{college.getId(), college.getConf(), college.getName()});
	}
}
