package com.combine.dal;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;

import com.combine.dao.CombineDao;
import com.combine.model.College;
import com.combine.model.Conference;
import com.combine.model.Participant;
import com.combine.model.WorkoutResult;

public class DataSourceLayer {
	private static DataSourceLayer instance;
	
	private CombineDao combineDao;
	
	private DataSourceLayer(DataSource dataSource){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		//configure flyway
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		String[] locations = new String[]{"classpath:migrations/"};
		flyway.setLocations(locations);
		flyway.migrate();
		
		//configure DAO's
		this.combineDao = new CombineDao(jdbcTemplate);
	}
	
	public static DataSourceLayer getInstance(){
		if(instance == null){
			instance = new DataSourceLayer(getDataSource());
		}
		return instance;
	}

	private static DataSource getDataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl("jdbc:postgresql://127.0.0.1:5432/NflCombine");
		ds.setUsername("postgres");
		ds.setPassword("admin");
		ds.setDriverClassName("org.postgresql.Driver");	
        return ds;
	}

	public CombineDao getCombineDao() {
		return combineDao;
	}
	
	public void clearDb(){
		this.combineDao.deleteWorkoutResult();
		this.combineDao.deleteParticipant();
	}
	
	public void addParticipants(List<Participant> participants){
		for (int i = 0; i < participants.size(); i++) {
			this.combineDao.insertParticipant(participants.get(i));
		}
	}
	
	public void addWorkoutResults(List<WorkoutResult> workoutResults){
		for (int i = 0; i < workoutResults.size(); i++) {
			this.combineDao.insertWorkoutResult(workoutResults.get(i));
		}
	}

	public void addConferences(List<Conference> conferences) {
		for (int i = 0; i < conferences.size(); i++) {
			this.combineDao.insertConference(conferences.get(i));
		}
	}

	public void addColleges(List<College> colleges) {
		for (int i = 0; i < colleges.size(); i++) {
			this.combineDao.insertCollege(colleges.get(i));
		}
	}
}
