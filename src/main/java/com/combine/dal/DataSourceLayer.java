package com.combine.dal;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.combine.profootballref.weekly.dao.WeeklyNflStatsDao;

public class DataSourceLayer {
	private static DataSourceLayer instance;
	
	private WeeklyNflStatsDao weeklyNflStatsDao;
	
	private DataSourceLayer(DataSource dataSource){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		//configure flyway
//		Flyway flyway = new Flyway();
//		flyway.setDataSource(dataSource);
//		String[] locations = new String[]{"classpath:migrations/"};
//		flyway.setLocations(locations);
//		flyway.migrate();
		
		//configure DAO's
		this.weeklyNflStatsDao = new WeeklyNflStatsDao(jdbcTemplate);
	}
	
	public static DataSourceLayer getInstance(){
		if(instance == null){
			instance = new DataSourceLayer(getDataSource());
		}
		return instance;
	}

	private static DataSource getDataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl("jdbc:postgresql://127.0.0.1:5432/NflStats");
		ds.setUsername("postgres");
		ds.setPassword("admin");
		ds.setDriverClassName("org.postgresql.Driver");	
        return ds;
	}

	public WeeklyNflStatsDao getWeeklyNflStatsDao() {
		return weeklyNflStatsDao;
	}
}
