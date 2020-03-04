package com.vetweb.api.config;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vetweb.api.model.mongo.Message;

public class MessageDeserializer implements Deserializer<Message> {
	
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public Message deserialize(String topic, byte[] data) {
		try {
			return mapper.readValue(data, Message.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
	}

}
