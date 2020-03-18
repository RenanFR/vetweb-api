package com.vetweb.api.pojo;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.vetweb.api.model.auth.User;

public class DTOConverter {
	
	public static UserToken userToDataTransferObject(User user) {
		return UserToken
				.builder()
				.id(user.getId())
				.userEmail(user.getEmail())
				.inclusionDate(user.getInclusionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.socialUser(user.isSocialLogin())
				.profiles(user.getAuthorities().stream().map(profile -> profile.getAuthority()).collect(Collectors.toList()))
				.build();
	}

}
