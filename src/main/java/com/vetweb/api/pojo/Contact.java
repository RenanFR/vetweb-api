package com.vetweb.api.pojo;

import java.util.List;

import com.vetweb.api.model.mongo.Message;

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
public class Contact {
	
	private UserToken user;
	
	private List<Message> messages;
	
	private boolean mostRecentContact;

}
