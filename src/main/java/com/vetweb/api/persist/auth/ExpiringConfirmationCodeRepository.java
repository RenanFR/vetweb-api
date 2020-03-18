package com.vetweb.api.persist.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vetweb.api.model.auth.ExpiringConfirmationCode;

@Repository
public interface ExpiringConfirmationCodeRepository  extends JpaRepository<ExpiringConfirmationCode, Long> {
	
	public ExpiringConfirmationCode findByCode(String code);
	
	public Optional<ExpiringConfirmationCode> findByUserId(Long userId);

}
