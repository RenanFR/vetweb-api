package com.vetweb.api.model.auth;

import lombok.Data;

@Data
public class AuthInfoDTO {
	
	private String email;
	
	private String password;
	
	private String authCode;

}
