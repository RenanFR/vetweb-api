package com.vetweb.api.persist.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vetweb.api.model.auth.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {
	
}
