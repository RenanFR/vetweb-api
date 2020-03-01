package com.vetweb.api.tests.kafka;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomDeserializer<T> implements Deserializer<T> {
	
	ObjectMapper mapper = new ObjectMapper();
	Class<T> type;
	public static final String TYPE_CONFIG = "type.config";

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		try {
			System.out.println(TYPE_CONFIG);
			this.type = (Class<T>) Class.forName(String.valueOf(configs.get(TYPE_CONFIG)));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Target class not found, cannot read");
		}
	}

	@Override
	public T deserialize(String topic, byte[] data) {
		try {
			return mapper.readValue(data, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
	}

}
