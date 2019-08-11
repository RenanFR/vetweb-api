package com.vetweb.api.service.auth;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.vetweb.api.model.auth.PasswordRecovery;
import com.vetweb.api.model.auth.User;
import com.vetweb.api.persist.auth.PasswordRecoveryRepository;

@Service
public class PasswordRecoveryService {
	
	@Autowired
	private PasswordRecoveryRepository repository;
	
	private static final int TIME_TO_EXPIRE = 1; 
	
	public PasswordRecovery generateRecoveryHash(@RequestBody User user) {
		List<PasswordRecovery> older = repository.searchByUser(user.getId());
		if (older.size() > 0) {
			PasswordRecovery recovery = older.get(0);
			recovery.setExpirationDate(getExpiration());
			PasswordRecovery saved = repository.save(recovery);
			return saved;
		}
		PasswordRecovery passwordRecovery = new PasswordRecovery();
		passwordRecovery.setExpirationDate(getExpiration());
		String hash = UUID.randomUUID().toString().replace("-", "");
		passwordRecovery.setHash(hash);
		passwordRecovery.setUser(user);
		PasswordRecovery saved = repository.save(passwordRecovery);
		return saved;
	}
	
	public List<PasswordRecovery> findByUser(Long userId) {
		return repository.searchByUser(userId);
	}
	
	public PasswordRecovery findByHash(String recoveryHash) {
		return repository.findByHash(recoveryHash);
	}
	
	public LocalDate getExpiration() {
		return LocalDate.now().plusDays(TIME_TO_EXPIRE);
	}
	
	public void cleanAfterConfirm(Long userId) {
		this.repository.deleteAll(this.findByUser(userId));
	}

}
