package com.combine.profootballref.weekly.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class WeeklyNflStatsDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public WeeklyNflStatsDao(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
}
