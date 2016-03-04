package com.combine.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversionService {
	
	public ConversionService(){}
	
	public Double toRawInches(String formattedInches){
		Pattern p1 = Pattern.compile("(\\d+)'(\\d+)\"");
		Pattern p2 = Pattern.compile("(\\d{1,2})(\\s)(\\d{1})/(\\d{1})\"");
		Pattern p3 = Pattern.compile("(\\d{1,2})\"");
		Pattern p4 = Pattern.compile("(\\d+)(\\s)(\\d+)/(\\d+)");
		
		Matcher m1 = p1.matcher(formattedInches);
		Matcher m2 = p2.matcher(formattedInches);
		Matcher m3 = p3.matcher(formattedInches);
		Matcher m4 = p4.matcher(formattedInches);
		
		Double result = null;
		if(m1.matches()){
			result = Double.parseDouble(m1.group(1)) * 12 + Double.parseDouble(m1.group(2));
		}
		else if(m2.matches()){
			result = Double.parseDouble(m2.group(1)) + ( Double.parseDouble(m2.group(3)) / Double.parseDouble(m2.group(4)) );
		}
		else if(m3.matches()){
			result = Double.parseDouble(m3.group(1));
		}
		else if(m4.matches()){
			result = Double.parseDouble(m4.group(1)) + ( Double.parseDouble(m4.group(3)) / Double.parseDouble(m4.group(4)) );
		}
		return result;
	}

	public Integer toRawLbs(String val) { 
		return Integer.parseInt(val.replaceAll("\\D+", ""));
	}
}
