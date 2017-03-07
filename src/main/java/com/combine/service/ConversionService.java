package com.combine.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.combine.dal.DataSourceLayer;
import com.combine.model.College;
import com.combine.model.Player;

public class ConversionService {
	
	private DataSourceLayer dataSourceLayer;
	
	public ConversionService(DataSourceLayer dataSourceLayer){
		this.dataSourceLayer = dataSourceLayer;
	}
	
	public Double toRawInches(String formattedInches){
		
		if(formattedInches == null){
			return 0.0;
		}
		
		Pattern p1 = Pattern.compile("(\\d+)'(\\d+)\"");
		Pattern p2 = Pattern.compile("(\\d{1,2})(\\s)(\\d{1})/(\\d{1})\"");
		Pattern p3 = Pattern.compile("(\\d{1,2})\"");
		Pattern p4 = Pattern.compile("(\\d+)(\\s)(\\d+)/(\\d+)");
		Pattern p5 = Pattern.compile("(\\d+)-(\\d+)");
		Pattern p6 = Pattern.compile("(\\d+)");
		
		Matcher m1 = p1.matcher(formattedInches);
		Matcher m2 = p2.matcher(formattedInches);
		Matcher m3 = p3.matcher(formattedInches);
		Matcher m4 = p4.matcher(formattedInches);
		Matcher m5 = p5.matcher(formattedInches);
		Matcher m6 = p6.matcher(formattedInches);
		
		Double result = null;
		try{
			if(m1.matches()){
				result = Double.parseDouble(m1.group(1)) * 12 + Double.parseDouble(m1.group(2));
			}
			else if(m2.matches()){
				result = Double.parseDouble(m2.group(1)) + ( Double.parseDouble(m2.group(3)) / Double.parseDouble(m2.group(4)) );
			}
			else if(m3.matches()){
				result = Double.parseDouble(m3.group(1));
			}
			else if(m6.matches()){
				result = Double.parseDouble(m6.group(1));
			}
			else if(m4.matches()){
				result = Double.parseDouble(m4.group(1)) + ( Double.parseDouble(m4.group(3)) / Double.parseDouble(m4.group(4)) );
			}
			else if(m5.matches()){
				result = Double.parseDouble(m5.group(1)) * 12 + Double.parseDouble(m5.group(2));
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public Integer toRawLbs(String val) { 
		return Integer.parseInt(val.replaceAll("\\D+", ""));
	}
	
	/**
	 * convert raw college name to identical or closest match in db, and return the id of that match
	 * @param value
	 * @return
	 */
	public Integer collegeNameToId(String value) {
		List<College> colleges = this.dataSourceLayer.allColleges();
		
		Optional<Integer> id = colleges.stream()
				.filter(c-> c.getName().equals(value))
				.map(c->c.getId())
				.findFirst();
			if(id.isPresent()){
				return id.get();
			}
			
		//specific mappings
		if(value.equals("Southern Methodist")){
			return colleges.stream()
					.filter(c-> c.getName().equals("SMU"))
					.map(c->c.getId())
					.findFirst()
					.get();
		}
		else if(value.equals("Brigham Young")){
			return colleges.stream()
					.filter(c-> c.getName().equals("BYU"))
					.map(c->c.getId())
					.findFirst()
					.get();
		} else if(value.equals("Southern California")){
			return colleges.stream()
					.filter(c-> c.getName().equals("USC"))
					.map(c->c.getId())
					.findFirst()
					.get();
		} else if(value.equals("Ole Miss")){
			return colleges.stream()
					.filter(c-> c.getName().equals("Mississippi"))
					.map(c->c.getId())
					.findFirst()
					.get();
		} else if(value.equals("UCF")){
			return colleges.stream()
					.filter(c-> c.getName().equals("Central Florida"))
					.map(c->c.getId())
					.findFirst()
					.get();
		} else if(value.equals("UL Lafayette")){
			return colleges.stream()
					.filter(c-> c.getName().equals("Louisiana-Lafayette"))
					.map(c->c.getId())
					.findFirst()
					.get();
		} else if(value.equals("Stephen F. Austin")){
			return colleges.stream()
					.filter(c-> c.getName().equals("Stephen F Austin"))
					.map(c->c.getId())
					.findFirst()
					.get();
		} else if(value.equals("UC Davis")){
			return colleges.stream()
					.filter(c-> c.getName().equals("Cal Davis"))
					.map(c->c.getId())
					.findFirst()
					.get();
		} else if(value.equals("UT Martin")){
			return colleges.stream()
					.filter(c-> c.getName().equals("Tennessee-Martin"))
					.map(c->c.getId())
					.findFirst()
					.get();
		} else if(value.equals("UTSA")){
			return colleges.stream()
					.filter(c-> c.getName().equals("Texas-San Antonio"))
					.map(c->c.getId())
					.findFirst()
					.get();
		}
			
		try{
			if(value.contains("(") && value.contains("Saint")){
				return colleges.stream()
						.filter(c->{
							return c.getName().contains("(") && c.getName().contains("St") && 
									value.substring(0,value.indexOf("(")+2).equals(c.getName().replace("St", "Saint").substring(0,c.getName().indexOf("(")+2));	
						})
						.map(c->c.getId())
						.findFirst()
						.get();
			} else if(value.contains("Saint")){
				return colleges.stream()
					.filter(c-> {
						return c.getName().contains("St") && c.getName().replaceAll("St", "Saint").equals(value);
					})
					.map(c->c.getId())
					.findFirst()
					.get();

			} else if(value.contains("State")){
				return colleges.stream()
					.filter(c-> {
						return c.getName().contains("St") && c.getName().replaceAll("St", "State").equals(value);
					})
					.map(c->c.getId())
					.findFirst()
					.get();
			} else if(value.contains("Mississippi")){
				return colleges.stream()
					.filter(c-> {
						return c.getName().contains("Miss") && c.getName().replaceAll("Miss", "Mississippi").equals(value);
					})
					.map(c->c.getId())
					.findFirst()
					.get();
						
			} else if(value.contains("(")){
				return colleges.stream()
						.filter(c->{
							return c.getName().contains("(") && 
									value.substring(0,value.indexOf("(")+2).equals(c.getName().substring(0,c.getName().indexOf("(")+2));	
						})
						.map(c->c.getId())
						.findFirst()
						.get();
			}
			return findSimilar(value, colleges);
		}
		catch(NoSuchElementException | NullPointerException e){
			System.out.println(e.getMessage());
			return findSimilar(value, colleges);
		}
	}
	
	/**
	 * helper method to the above findCollege method. this method is called if no specific case is matched and a similar by character method is necessary.
	 * like above, it also returns the college id (or null if nothing is close to matching) of the closest matching college 
	 * @param value
	 * @param colleges
	 * @return
	 */
	private Integer findSimilar(String value, List<College> colleges){
		Map<Integer, College> similar = new HashMap<>();
		value = value.replace("Saint", "St");
		value = value.replace("State", "St");
		for(College c : colleges){
			int sum = 0;
			for(int i = 0; i < Math.min(value.length(),c.getName().length()); i++){
				sum += Math.abs((int)c.getName().toLowerCase().charAt(i) - (int)value.toLowerCase().charAt(i));
			}
			if(similar.size() == 0){
				similar.put(sum, c);
			} else{
				for(Entry<Integer, College> entry : similar.entrySet()){
					if(sum < entry.getKey()){
						similar = new HashMap<>();
						similar.put(sum, c);
					}
				}
			}
		}
		for(Entry<Integer, College> entry : similar.entrySet()){
			return entry.getKey() < 35 && entry.getValue().getName().toLowerCase().substring(0,2).equals(value.toLowerCase().substring(0, 2)) ? entry.getValue().getId() : null;
		}
		return null;
	}

	public Player findPlayerByNflData(String firstname, String lastname, String college, String conference,
			String position) {
		
		return this.dataSourceLayer.getCombineDao().findByAttributes(firstname.toLowerCase(), lastname.toLowerCase(),
				college.toLowerCase(), conference.toLowerCase(), position.toLowerCase());
	}
	
}
