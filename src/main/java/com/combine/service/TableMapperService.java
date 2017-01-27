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
	
	private GenericsService genericsService;
	
	public TableMapperService(GenericsService genericsService){
		this.genericsService = genericsService;
	}

	/**
	 * parse a single row and return the result as a map<integer,string>, where the integer is the index and string the text value
	 * @param elements
	 * @return
	 */
	public Map<Integer, String> parseTableHeaderRow(Elements elements) {
		Map<Integer, String> headers = new HashMap<>();
		for (int i = 0; i < elements.size(); i++) {
			String header = elements.get(i).attr("data-stat");
			headers.put(i, header);
		}
		return headers;
	}

	/**
	 * parse table row by field level annotation value
	 * @param headers
	 * @param tdElements
	 * @param obj
	 * @param firstColumnOffset
	 */
	public <T> void parseTableRow(Map<Integer, String> headers, List<Element> tdElements, T obj, int firstColumnOffset, boolean stripTags) {
		String value = "";
		for (int k = 0; k < tdElements.size(); k++) {
			try {
				if (tdElements.get(k).getElementsByTag("a").size() > 0) {
					value = stripTags(tdElements.get(k).html());
				} else {
					value = tdElements.get(k).html();
				}

				if (!StringUtils.isEmpty(value)) {
					value = value.replace("%", "");
					Field field = genericsService.getField(obj.getClass(), headers.get(k + firstColumnOffset));
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

	public String stripTags(String html){
		StringBuilder sb = new StringBuilder();
		int pos = 1;
		while(html.indexOf(">",pos) > -1 && html.indexOf("<", pos) > -1){
			sb.append(html.substring(html.indexOf(">", pos)+1, html.indexOf("<", pos)));
			pos = html.indexOf(">", html.indexOf("<", pos)+1); //move cursor past closing tag
		}
		//add remainder
		sb.append(html.substring(pos+1));
		return sb.toString();
	};
}
