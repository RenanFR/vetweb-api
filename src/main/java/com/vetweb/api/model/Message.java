package com.vetweb.api.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
	
	private String content;
	
	private String sender;
	
	private LocalDateTime sentAt;

}
