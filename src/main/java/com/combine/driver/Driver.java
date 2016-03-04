package com.combine.driver;

import com.combine.dal.DataSourceLayer;
import com.combine.service.CombineParserService;

public class Driver {

	public static void main(String[] args){		
		DataSourceLayer dataSource = DataSourceLayer.getInstance();

		CombineParserService parser = new CombineParserService(dataSource);
//		parser.insertColleges();
		parser.parse();
	}
	
}
