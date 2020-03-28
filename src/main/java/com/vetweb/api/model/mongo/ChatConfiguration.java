package com.vetweb.api.model.mongo;

import java.time.LocalDateTime;

import com.vetweb.api.pojo.UserToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatConfiguration {
	
	private String _id;
	
	private UserToken user;
	
	private AutomaticResponse automaticResponse;
	
	private LocalDateTime changedAt;
	
}
