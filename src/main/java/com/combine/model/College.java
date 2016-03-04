package com.combine.model;

public class College {

	private Integer id;
	private Integer conf;
	private String name;

	public College() {
	}

	public College(Integer id, Integer conf, String name) {
		this.id = id;
		this.conf = conf;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getConf() {
		return conf;
	}

	public void setConf(Integer conf) {
		this.conf = conf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
