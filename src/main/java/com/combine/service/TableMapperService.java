package com.combine.service;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

/**
 * Handle conversion of html tables to java objects
 * @author lancefallon
 *
 */
public class TableMapperService {
	
	Logger LOGGER = Logger.getLogger(TableMapperService.class);
	
	private GenericService genericService;
	
	public TableMapperService(GenericService genericService){
		this.genericService = genericService;
	}

	public Map<Integer, String> parseTableHeaderRow(Elements elements) {
		Map<Integer, String> headers = new HashMap<>();
		for (int i = 0; i < elements.size(); i++) {
			String header = elements.get(i).attr("data-stat");
			headers.put(i, header);
		}
		return headers;
	}

	public <T> void parseTableRow(Map<Integer, String> headers, List<Element> tdElements, T obj, int firstColumnOffset) {
		String value = "";
		for (int k = 0; k < tdElements.size(); k++) {
			try {
				if (tdElements.get(k).getElementsByTag("a").size() > 0) {
					value = tdElements.get(k).getElementsByTag("a").html();
				} else {
					value = tdElements.get(k).html();
				}

				if (!StringUtils.isEmpty(value)) {
					value = value.replace("%", "");
					Field field = genericService.getField(obj.getClass(), headers.get(k + firstColumnOffset));
					field.setAccessible(true);
					if (field.getType().equals(String.class)) {
						field.set(obj, value);
					} else if (field.getType().equals(Integer.class)) {
						field.set(obj, Integer.valueOf(value));
					} else if (field.getType().equals(Double.class)) {
						field.set(obj, Double.valueOf(value));
					} else if (field.getType().equals(Date.class)) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Date date = df.parse(value);
						field.set(obj, date);
					}
				}

			} catch (Exception e) {
				LOGGER.log(Level.DEBUG, e.getMessage());
			}
		}
	}
}
