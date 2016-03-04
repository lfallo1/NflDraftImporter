package com.combine.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Generic row mapper. The mapper assumes there is a constructor with all fields in the class 
 * as its arguments, and ordered in the same order as they are returned from the sql
 * statement. It will handle classes containing primitive types or their native wrappers.
 * The mapper could be improved by creating custom annotations on the classes for a more 
 * fine-grained / flexible mapping. But for now, use as described above.
 * @author lancefallon
 *
 * @param <T>
 */
public class GenericMapper<T> implements RowMapper<T> {
	
    final Class<T> classType;

    public GenericMapper(Class<T> typeParameterClass) {
        this.classType = typeParameterClass;
    }

	@SuppressWarnings("unchecked")
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		//Set constructor to the constructor with same number of args as class has
		Constructor<?> constr = null;
		for (Constructor<?> c : classType.getConstructors()) {
			if(c.getParameterCount() == this.classType.getDeclaredFields().length){
				constr = c;
			}
		}
		//Declare an array of objects that will be passed as the parameter when creating the object
		Object[] args = new Object[constr.getParameterTypes().length];

		//Set the arguments		
		for (int i = 0; i < constr.getParameterCount(); i++) {
			try{
				//get the next item in result set and cast to type of argument in constructor
				args[i] = constr.getParameterTypes()[i].cast(rs.getObject(i+1));
			}
			catch(ClassCastException e){
				try{
					//check for integer to long conversion value
					args[i] = ((Long)rs.getObject(i+1)).intValue();
				}
				catch(Exception ex){
					//if conversion still fails, we lost... just return null
					return null;
				}
			}	
		}
		
		try {
			return (T) constr.newInstance(args);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}	
	}
}

