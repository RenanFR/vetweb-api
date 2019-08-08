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
 * This controller handles requests related to authentication, authorization and user registration for this API
 *
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
	
	/**
	 * Creates a new user account based on provided DTO 
	 * @param User account DTO with basic sign up information like user name, password, e-mail, etc.
	 * @return A QR code if two factor authentication is enabled or a simple success message
	 * @throws Should handle when user already exists or required parameters like e-mail are missing
	 */
	@PostMapping("signup")
	public ResponseEntity<String> createAccount(@RequestBody NewUserDTO account) {
		LOGGER.info("Creating user " + account);
		User user = new User(account.getUserName(), account.getUserMail(), account.getPassword(), account.isUseTwoFactorAuth(), account.isSocialLogin());
		String qrCodeOrMessage = userService.signUp(user);
		return ResponseEntity.ok(qrCodeOrMessage);
	}
	
	/**
	 * Handles authentication process
	 * @param User name and password passed via request body
	 * @return Application token for user session
	 */

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

	/**
	 * Checks if user with provided e-mail exists, used as async validation on user form to display messages  
	 * @param User e-mail to check against the database
	 * @return Boolean value corresponding to whether user exists or not
	 */
	@GetMapping("exists/{user}")
	public boolean userExists(@PathVariable("user") String user) {
		boolean doesUserExists = userService.userExists(user);
		return doesUserExists;
	}

	/**
	 * Check if provided user has enabled two factor authentication, used as async validation to show or hide TFA code control
	 * @param User e-mail to check against the database
	 * @return Boolean value corresponding to whether user enabled TFA or not
	 */
	@GetMapping("uses-tfa/{user}")
	public ResponseEntity<Boolean> checkTFAIsEnabledForUser(@PathVariable("user") String user) {
		User account = userService.findByEmail(user);
		boolean using2fa = account != null? account.isUsing2FA() : false;
		return ResponseEntity.ok(using2fa);
	}
	
	/**
	 * Build a map with user name and provided application session token (JWT) to be used as login return to front end
	 * @param User name authenticated and generated JWT
	 * @return Map with both information to serve as request return to front end
	 */
	private Map<String, String> buildUserInformationMap(String user, String appToken) {
		Map<String, String> userInformationMap = new HashMap<String, String>();
		userInformationMap.put("username", user);
		userInformationMap.put("token", appToken);
		return userInformationMap;
	}	
	
}
