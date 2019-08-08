package com.vetweb.api.resources;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.config.auth.TokenService;
import com.vetweb.api.model.auth.NewUserDTO;
import com.vetweb.api.model.auth.User;
import com.vetweb.api.service.auth.UserService;

/**
 * @author Renan Rodrigues
 */
@RestController
@RequestMapping("account")
public class AccountController {
	
	@Autowired
	private AuthenticationManager authenticationManager;	
	
	@Autowired
	private UserService userService;
	
	@Value("${id.client.oauth}")
	private String idClientOauth;	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	
	@PostMapping("signup")
	public ResponseEntity<String> createAccount(@RequestBody NewUserDTO account) {
		LOGGER.info("Creating user " + account);
		User user = new User(account.getUserName(), account.getUserMail(), account.getPassword(), account.isUseTwoFactorAuth(), account.isSocialLogin());
		String qrCodeOrMessage = userService.signUp(user);
		return ResponseEntity.ok(qrCodeOrMessage);
	}
	

	@PostMapping
	public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
		try {
			String email = user.getEmail();
			String password = (!user.isSocialLogin()) ? user.getPassword() : "";
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			UserDetails loadUser = userService.loadUserByUsername(email);
			String token = TokenService.createToken(email);
			Map<String, String> userInformationMap = buildUserInformationMap(loadUser.getUsername(), token);
			return ok(userInformationMap);
		} catch (AuthenticationException authenticationException) {
			throw new BadCredentialsException("Authentication data provided is invalid");
		}
	}	

	@GetMapping("exists/{user}")
	public boolean userExists(@PathVariable("user") String user) {
		boolean doesUserExists = userService.userExists(user);
		return doesUserExists;
	}

	@GetMapping("uses-tfa/{user}")
	public ResponseEntity<Boolean> checkTFAIsEnabledForUser(@PathVariable("user") String user) {
		User account = userService.findByEmail(user);
		boolean using2fa = account != null? account.isUsing2FA() : false;
		return ResponseEntity.ok(using2fa);
	}
	
	private Map<String, String> buildUserInformationMap(String user, String appToken) {
		Map<String, String> userInformationMap = new HashMap<String, String>();
		userInformationMap.put("username", user);
		userInformationMap.put("token", appToken);
		return userInformationMap;
	}	
	
}
