package com.vetweb.api.config.json;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vetweb.api.model.mongo.Message;

public class MessageMVCSerializer extends JsonSerializer<Message> {

	@Override
	public void serialize(Message message, JsonGenerator generator, SerializerProvider serializers) throws IOException {
		generator.writeStartObject();
		generator.writeStringField("_id", message.get_id());
		generator.writeStringField("text", message.getContent());
		generator.writeStringField("sentAt", message.getSentAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		generator.writeStringField("sender", message.getSender());
		generator.writeNumberField("idSender", message.getIdSender());
		generator.writeStringField("receiver", message.getReceiver());
		generator.writeNumberField("idReceiver", message.getIdReceiver());
		generator.writeEndObject();
	}
	
}
