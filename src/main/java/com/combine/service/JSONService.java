package com.combine.service;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

public class JSONService {
	
	private static final Logger logger = Logger.getLogger(JSONService.class);
	
	public Integer getIntFromJSON(JSONObject obj, String key) {
		try {
			return obj.getInt(key);
		} catch (JSONException e) {
			return null;
		}
	}

	public Double getDoubleFromJSON(JSONObject obj, String key) {
		try {
			return obj.getDouble(key);
		} catch (JSONException e) {
			return null;
		}
	}

	public String getStringFromJSON(JSONObject obj, String key) {
		try {
			return obj.getString(key);
		} catch (JSONException e) {
			return null;
		}
	}

	public String loadJson(String filename) {
		StringBuilder sb = new StringBuilder();
		try {
			Scanner scanner = new Scanner(new ClassPathResource(filename).getFile());
			while (scanner.hasNextLine()) {
				sb.append(scanner.nextLine());
			}

			scanner.close();
		} catch (Exception e) {
			logger.warn(e.toString());
		}
		return sb.toString();
	}
}
