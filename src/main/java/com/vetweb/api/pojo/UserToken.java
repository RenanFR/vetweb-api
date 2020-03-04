package com.vetweb.api.pojo;

import java.util.List;

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
public class UserToken {
	
	private Long id;
	
	private String sub;
	
	private String userEmail;
	
	private boolean socialUser;
	
	private String inclusionDate;
	
	private List<String> profiles;

}
