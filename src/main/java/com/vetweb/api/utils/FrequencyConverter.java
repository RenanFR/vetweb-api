package com.vetweb.api.utils;

import java.time.Period;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FrequencyConverter implements AttributeConverter<Period, String> {

	@Override
	public String convertToDatabaseColumn(Period attribute) {
		return attribute.toString();
	}

	@Override
	public Period convertToEntityAttribute(String dbData) {
		return Period.parse(dbData);
	}

}
