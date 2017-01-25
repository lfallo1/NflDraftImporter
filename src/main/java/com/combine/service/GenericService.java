package com.combine.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import com.combine.annotations.StatField;

public class GenericService {
	
	public <E> E createInstance(Class<E> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}
	
	public <T> Field getField(Class<T> myClass, String fieldName) throws Exception {
		Field[] fields = myClass.getDeclaredFields();
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
		
		fields = myClass.getSuperclass().getDeclaredFields();
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
	
	//given a string, variable name (the field to be interpolated), and a value, perform some dirty interpolation
	public String interpolate(String string, String target, String value){
		String interpolated = string.replaceAll(Pattern.quote("${"+ target +"}"), value);
		return interpolated;
	}
}
