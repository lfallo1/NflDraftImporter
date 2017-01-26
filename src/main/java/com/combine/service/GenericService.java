package com.combine.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.combine.annotations.StatField;

/**
 * utilities related to generics / reflection
 * @author lancefallon
 *
 */
public class GenericService {
	
	public <E> E createInstance(Class<E> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}
	
	/**
	 * combine a list of native arrays
	 * @param list
	 * @param clazz
	 * @return
	 */
	public <T> T[] combineArrays(List<T[]> list, Class<T> clazz){
		
		//get length of array and declare new array of that size
		int length = list.stream().mapToInt(l->l.length).sum();
		@SuppressWarnings({ "unchecked" })
		T[] combined = (T[]) Array.newInstance(clazz, length);
		
		//combine arrays into a single array
		int position = 0;
		for(T[] item : list){
			System.arraycopy(item, 0, combined, position, item.length);
			position += item.length;
		}
		return combined;
	}
	
	/**
	 * return the field object on a specified class by its annotated field name
	 * @param myClass
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public <T> Field getField(Class<T> myClass, String fieldName) throws Exception {
		
		//add a list of declare fields and fields in super classes
		List<Field[]> fieldsArrayList = new ArrayList<>();
		fieldsArrayList.add(myClass.getDeclaredFields());
		
		//adding super classes here
		while(myClass.getSuperclass() != null){
			fieldsArrayList.add(myClass.getSuperclass().getDeclaredFields());
			myClass = (Class<T>) myClass.getSuperclass();
		}
		
		//combine all fields
		Field[] fields = this.<Field>combineArrays(fieldsArrayList, Field.class);
		
		//find field by annotation value
		for (Field field : fields) {
			Annotation[] annotations = field.getDeclaredAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().equals(StatField.class)) {
					StatField fieldAnnotation = (StatField) annotation;
					if (fieldAnnotation.value().equals(fieldName)) {
						return field;
					}
				}
			}
		}
		
		throw new Exception("Field not found");
	}
	
	/**
	 * given a string, variable name (the field to be interpolated), and a value, perform some dirty interpolation
	 * @param string
	 * @param target
	 * @param value
	 * @return
	 */
	public String interpolate(String targetString, Map<String, String> interpolationMap){
		for(String entry : interpolationMap.keySet()){
			targetString = targetString.replaceAll(Pattern.quote("${"+ entry +"}"), interpolationMap.get(entry));
		}
		return targetString;
	}
}
