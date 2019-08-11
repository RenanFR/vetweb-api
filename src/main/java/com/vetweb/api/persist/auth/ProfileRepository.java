package com.vetweb.api.persist.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vetweb.api.model.auth.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
	
}
