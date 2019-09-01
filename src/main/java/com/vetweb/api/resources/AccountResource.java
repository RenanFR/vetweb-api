package com.vetweb.api.resources;

import static org.springframework.http.ResponseEntity.ok;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.config.auth.TokenService;
import com.vetweb.api.exception.EmailException;
import com.vetweb.api.model.auth.AuthInfoDTO;
import com.vetweb.api.model.auth.NewUserDTO;
import com.vetweb.api.model.auth.PasswordRecovery;
import com.vetweb.api.model.auth.User;
import com.vetweb.api.pojo.SimpleMessageResponse;
import com.vetweb.api.service.PostmarkMailSender;
import com.vetweb.api.service.auth.PasswordRecoveryService;
import com.vetweb.api.service.auth.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Renan Rodrigues
 */
@RestController
@RequestMapping("/account")
@Api(value = "Handles authentication and authorization requests and provide useful user information")
public class AccountResource {
	
	@Autowired
	private AuthenticationManager authenticationManager;	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostmarkMailSender postmarkMailSender;
	
	@Autowired
	private PasswordRecoveryService recoveryService;
	
	@Value("${id.client.oauth}")
	private String idClientOauth;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountResource.class);
	
	@ApiOperation(
			value = "Sign up new user",
			notes = "Creates a new user to use VetWeb using provided DTO")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success message or QR code to scan as a secret to two factor authentication"),
		@ApiResponse(code = 400, message = "Error message if user name, email or password is not present in the request")
	})
	@PostMapping("signup")
	public ResponseEntity<String> createAccount(@RequestBody NewUserDTO account) {
		LOGGER.info("Creating user " + account);
		if ((account.getUserName() == null || account.getUserName().isEmpty()) || (account.getUserMail() == null || account.getUserMail().isEmpty()) || (account.getPassword() == null || account.getPassword().isEmpty())) {
			return ResponseEntity.badRequest().body("Missing required information");
		}
		User user = new User(account.getUserName(), account.getUserMail(), account.getPassword(), account.isUseTwoFactorAuth(), account.isSocialLogin());
		String qrCodeOrMessage = userService.signUp(user);
		return ResponseEntity.ok(qrCodeOrMessage);
	}
	
	@ApiOperation(
			value = "Update existent user",
			notes = "Update user information using provided DTO")
	@PutMapping("update")
	public ResponseEntity<String> updateUser(@RequestBody NewUserDTO account) {
		User user = userService.findByEmail(account.getUserMail());
		user.setPassword(account.getPassword());
		this.userService.saveUser(user);
		this.recoveryService.cleanAfterConfirm(user.getId());
		return ResponseEntity.accepted().body("The new password was set for user successfully");
	}
	
	@ApiOperation(
			value = "Find user by recovery hash",
			notes = "Find the user using a specific password recovery hash")
	@GetMapping("using-hash/{user}")
	public ResponseEntity<User> findUserByRecoveryHash(@PathVariable("user") String recoveryHash) {
		PasswordRecovery passwordRecovery = recoveryService.findByHash(recoveryHash);
		if (passwordRecovery != null) {
			boolean expired = passwordRecovery.getExpirationDate().isBefore(LocalDate.now());
			if (!expired) {
				return ResponseEntity.ok(passwordRecovery.getUser());
			}
		}
		return ResponseEntity.noContent().build();
	}
	

	@ApiOperation(
			value = "Sign in with existent user",
			notes = "Authenticate user with provided credentials checking if he exists and has enabled access")
	@PostMapping(value = "signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Map<String, String>> login(@RequestBody AuthInfoDTO user) {
		try {
			String email = user.getEmail();
			String password = user.getPassword();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			UserDetails loadUser = userService.loadUserByUsername(email);
			String token = TokenService.createToken(email);
			Map<String, String> userInformationMap = buildUserInformationMap(loadUser.getUsername(), token);
			return ok(userInformationMap);
		} catch (AuthenticationException authenticationException) {
			throw new BadCredentialsException("Authentication data provided is invalid");
		}
	}	

	@ApiOperation(
			value = "Does this user exists?",
			notes = "Checks if user with provided email exists in the database")
	@GetMapping("exists/{user}")
	public boolean userExists(@PathVariable("user") String user) {
		boolean doesUserExists = userService.userExists(user);
		return doesUserExists;
	}

	@ApiOperation(
			value = "User uses tfa?",
			notes = "Check if user with provided email uses two factor authentication or not")
	@GetMapping("uses-tfa/{user}")
	public ResponseEntity<Boolean> checkTFAIsEnabledForUser(@PathVariable("user") String user) {
		User account = userService.findByEmail(user);
		boolean using2fa = account != null? account.isUsing2FA() : false;
		return ResponseEntity.ok(using2fa);
	}
	
	@ApiOperation(
			value = "I forgot my password",
			notes = "Send email to user with valid recovery hash to set a new password")
	@PostMapping("forget")
	public ResponseEntity<SimpleMessageResponse> sendForgetPasswordEmail(@RequestBody String userEmail) {
		User user = userService.findByEmail(userEmail);
		try {
			postmarkMailSender.sendForgotPasswordEmail(user);
		} catch (RuntimeException exception) {
			throw new EmailException(exception.getMessage());
		}
		return ResponseEntity.ok(new SimpleMessageResponse("The request to reset the password was successfully sent to your email, check your mailbox in a couple of seconds and follow the steps provided"));
	}
	
	@ApiOperation(
			value = "Check user recovery",
			notes = "Checks if user with provided email has already requested password recovery and has a valid hash")
	@GetMapping("has-valid-hash/{user}")
	public ResponseEntity<Boolean> checkIfUserHasAlreadyRequestedANewPassword(@PathVariable("user") String userEmail) {
		User user = userService.findByEmail(userEmail);
		boolean hasValidHash = user != null? !recoveryService.findByUser(user.getId()).isEmpty() : false;
		return ResponseEntity.ok(hasValidHash);
	}
	
	private Map<String, String> buildUserInformationMap(String user, String appToken) {
		Map<String, String> userInformationMap = new HashMap<String, String>();
		userInformationMap.put("username", user);
		userInformationMap.put("token", appToken);
		return userInformationMap;
	}
	
}
