package com.vetweb.api.model.auth;

import lombok.Data;

@Data
public class NewUserDTO {
	
	private String userMail;
	
	private String userName;
	
	private String confirmationCode;
	
	private String temporaryPassword;
	
	private String password;
	
	private String passwordConfirmation;
	
	private boolean useTwoFactorAuth;
	
	private boolean isSocialLogin;

}
