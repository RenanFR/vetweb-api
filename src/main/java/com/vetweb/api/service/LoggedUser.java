package com.vetweb.api.service;

import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedUser {
	
	public static String get() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String user = principal.toString();
		return user;
	}

}
