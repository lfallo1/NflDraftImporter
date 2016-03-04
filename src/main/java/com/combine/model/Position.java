package com.combine.model;

import java.util.List;

public class Position {

	private Integer id;
	private Integer category;
	private String name;

	public Position() {
	}

	public Position(Integer id, String name, Integer category) {
		this.id = id;
		this.category = category;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Integer stringToId(String position, List<Position> positions) {
		for (int i = 0; i < positions.size(); i++) {
			if(positions.get(i).getName().equals(position)){
				return positions.get(i).getId();
			}
		}
		return null;
	}

}
