package com.combine.profootballref.weekly.service;

import java.util.List;

import com.combine.profootballref.weekly.dto.Team;

/**
 * help with converting dtos to more db compatible objects
 * @author lancefallon
 *
 */
public class DataConversionService {
	
	public Team findByTeamIdentifier(String teamIdentifier, List<Team> teams){
		return teams.stream().filter(t->t.getTeamIdentifier().toLowerCase().equals(teamIdentifier.toLowerCase())).findFirst().get();
	}
	
	public Team findByTeamName(String teamName, List<Team> teams){
		return teams.stream().filter(t->t.getTeamName().toLowerCase().indexOf(teamName.toLowerCase()) > -1).findFirst().get();
	}	

}
