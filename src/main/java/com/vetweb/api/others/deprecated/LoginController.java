package com.vetweb.api.others.deprecated;

import static org.springframework.http.ResponseEntity.ok;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.vetweb.api.config.auth.TokenService;
import com.vetweb.api.model.auth.GmailUser;
import com.vetweb.api.model.auth.GmailUser.GmailUserBuilder;
import com.vetweb.api.model.auth.Profile;
import com.vetweb.api.model.auth.User;
import com.vetweb.api.service.auth.UserService;

@Deprecated
@RestController
@RequestMapping("login")
public class LoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;

	@Value("${id.client.oauth}")
	private String idClientOauth;
	
	/**
	 * Validate if token provided by google authentication process corresponds to a real account
	 * @param Encoded token provided by google via social login library
	 * @return Application token manually generated for social media user session
	 */
	@PostMapping("google")
	public ResponseEntity<Map<String, String>> handleGoogleToken(@RequestBody String tokenGmail) 
			throws IOException, GeneralSecurityException {
		GoogleIdTokenVerifier tokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
				.setAudience(Collections.singletonList(this.idClientOauth))
				.build();
		GoogleIdToken idToken = tokenVerifier.verify(tokenGmail);
		if (idToken != null) {
			Payload payload = idToken.getPayload();
			GmailUser gmailUser = this.setUserFromPayload(payload);
			User user = checkGmailUser(gmailUser);
			String appToken = this.login(user).getBody().get("token");
			Map<String, String> userInformationMap = buildUserInformationMap(user.getName(), appToken);
			return ResponseEntity.ok(userInformationMap);
		}
		return null;
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
	 * Maps user information from gmail token payload into a custom DTO
	 * @param Gmail token payload
	 * @return GmailUser DTO encapsulating information like email, picture and name extracted from token
	 */
	private GmailUser setUserFromPayload(Payload payload) {
		GmailUser gmailUser = new GmailUserBuilder()
			.withEmail(payload.get("email").toString())
			.withName(payload.get("name").toString())
			.withPicture(payload.get("picture").toString())
			.withJti(payload.get("jti").toString())
			.withLocale(payload.get("locale").toString())
			.withSub(payload.get("sub").toString())
			.withIss(payload.get("iss").toString())
			.withExp(payload.get("exp").toString())
			.withIat(payload.get("iat").toString())
			.withAud(payload.get("aud").toString())
			.build();
		return gmailUser;
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
	
	/**
	 * Save social media user that had sign up using his gmail account
	 * @param Gmail user DTO obtained after mapping the token
	 * @return New user record on database
	 */
	private User saveSocialUser(GmailUser gmailUser) {
		User user = new User();
		user.setName(gmailUser.getName());
		user.setPassword("");
		user.setSocialLogin(true);
		user.setInclusionDate(LocalDate.now());
		user.setEmail(gmailUser.getEmail());
		Set<Profile> roleProfile = Collections.singleton(userService.findProfileByDescription("ROLE"));
		user.setProfiles(new ArrayList<Profile>(roleProfile));
		userService.saveUser(user);
		return user;
	}
	
	/**
	 * Checks if provided gmail account was already recorded in database or should be inserted 
	 * @param Gmail user DTO obtained after mapping the token
	 * @return New user record on database or existent record obtained by querying
	 */
	private User checkGmailUser(GmailUser gmailUser) {
		User onDatabase = userService.findByName(gmailUser.getName());
		if (onDatabase == null) {
			return saveSocialUser(gmailUser);
		}
		return onDatabase;
	}	

}
