package com.vetweb.api.persist.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vetweb.api.model.auth.PasswordRecovery;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long> {
	
	String QUERY_BY_USER = " SELECT * FROM tbl_password_recovery pr WHERE pr.user_id = :userId";	
	
	public PasswordRecovery findByHash(String hash);
	
	@Query(value = QUERY_BY_USER, nativeQuery = true)
	public List<PasswordRecovery> searchByUser(@Param("userId")Long user);

}
