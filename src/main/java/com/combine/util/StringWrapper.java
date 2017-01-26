package com.combine.util;

/**
 * Basic string wrapper.
 * Currently only has a contains & getter
 * @author lfallon
 *
 */
public class StringWrapper {

	private final String s;
	
	public StringWrapper(String s){
		this.s = s;
	}
	
	/**
	 * get the string
	 * @return
	 */
	public String get(){
		return s;
	}
	
	/**
	 * return whether or not the string contains / "has" the passed in string
	 * @param str
	 * @return
	 */
	public boolean has(String str){
		return s.indexOf(str) > -1;
	}
	
}
