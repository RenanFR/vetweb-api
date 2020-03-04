package com.vetweb.api.model.mongo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vetweb.api.config.json.MessageMVCDeserializer;
import com.vetweb.api.config.json.MessageMVCSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize(using = MessageMVCDeserializer.class)
@JsonSerialize(using = MessageMVCSerializer.class)
@Document(collection = "messages")
public class Message {
	
	private String _id;
	
	private String content;
	
	private String sender;
	
	private Long idSender;
	
	private LocalDateTime sentAt;
	
	private String receiver;
	
	private Long idReceiver;

	@Override
	public String toString() {
		return "Message [content=" + content + ", sender=" + sender + ", sentAt=" + sentAt + "]";
	}

}
