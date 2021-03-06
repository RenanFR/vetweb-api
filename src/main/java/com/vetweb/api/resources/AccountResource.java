package com.vetweb.api.resources;

import static com.vetweb.api.pojo.DTOConverter.userToDataTransferObject;
import static org.springframework.http.ResponseEntity.ok;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.config.auth.TokenService;
import com.vetweb.api.exception.EmailException;
import com.vetweb.api.model.auth.AuthInfoDTO;
import com.vetweb.api.model.auth.ExpiringConfirmationCode;
import com.vetweb.api.model.auth.NewUserDTO;
import com.vetweb.api.model.auth.PasswordRecovery;
import com.vetweb.api.model.auth.User;
import com.vetweb.api.model.mongo.Message;
import com.vetweb.api.persist.auth.ExpiringConfirmationCodeRepository;
import com.vetweb.api.pojo.Contact;
import com.vetweb.api.pojo.SimpleMessageResponse;
import com.vetweb.api.pojo.UserToken;
import com.vetweb.api.service.MessageService;
import com.vetweb.api.service.auth.PasswordRecoveryService;
import com.vetweb.api.service.auth.UserService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.bytebuddy.utility.RandomString;

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
	private PasswordRecoveryService recoveryService;
	
	@Autowired
	private MeterRegistry registry;
	
	private Counter nonexistentCounter;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ExpiringConfirmationCodeRepository codeRepository;
	
	@Value("${id.client.oauth}")
	private String idClientOauth;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountResource.class);
	
	@PostConstruct
	public void initializeCounters() {
		nonexistentCounter = registry.counter("nonexistent.user.check");
	}	
	
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
		if ((account.getUserName() == null || account.getUserName().isEmpty()) || (account.getUserMail() == null || account.getUserMail().isEmpty())) {
			return ResponseEntity.badRequest().body("Missing required information");
		}
		String firstAccessPassword = RandomString.make(6);
		User user = new User(account.getUserName(), account.getUserMail(), firstAccessPassword, account.isUseTwoFactorAuth(), account.isSocialLogin());
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
			registry.counter("user.authentication.count", "email", email).increment();
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
		if (!doesUserExists) {
			nonexistentCounter.increment();
		}
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
			userService.forgotPassword(user);
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
	
	@GetMapping("contacts")
	public ResponseEntity<List<Contact>> myContacts(@RequestParam("currentUser") String currentUser) {
		final User mostRecentContact;
		List<User> contacts = userService.findContactsFor(currentUser);
		List<Contact> contactList = contacts.
			stream()
			.map((User contact) -> {
				UserToken userToken = userToDataTransferObject(contact);
				List<Message> messages = messageService.messagesFromUser(userToken.getUserEmail());
				messages.sort((Message one, Message other) -> one.getSentAt().compareTo(other.getSentAt()));
				return Contact.builder().user(userToken).messages(messages).build();
			})
			.collect(Collectors.toList());
		List<Message> allMessages = new ArrayList<>();
		contactList.forEach((Contact contact) -> {
			allMessages.addAll(contact.getMessages().stream().filter(msg -> msg.getReceiver().equals(currentUser)).collect(Collectors.toList()));
		});
		allMessages.sort((Message one, Message other) -> one.getSentAt().compareTo(other.getSentAt()));		
		mostRecentContact = !allMessages.isEmpty()? contacts.stream().filter(u -> u.getEmail().equals(allMessages.get(0).getSender())).findFirst().get() : contacts.get(0);
		contactList.forEach((Contact contact) -> {
			if (contact.getUser().getUserEmail().equals(mostRecentContact.getEmail())) {
				contact.setMostRecentContact(true);
			}
		});
		return ResponseEntity.ok(contactList);
	}
	
	@GetMapping("{userId}")
	public ResponseEntity<User> checkUserToConfirm(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(this.userService.findById(userId));
	}
	
	@PutMapping("confirm")
	public ResponseEntity<Boolean> confirmAccount(@RequestBody NewUserDTO account) {
		User user = userService.findByEmail(account.getUserMail());
		ExpiringConfirmationCode code = codeRepository.findByCode(account.getConfirmationCode());
		if (code.isValid()) {
			user.setPassword(account.getPassword());
			user.setTemporaryPassword("NOT_SET");
			user.setEnabled(true);
			codeRepository.delete(code);
			this.userService.saveUser(user);
			
		}
		return ResponseEntity.ok(true);
	}
	
	@PostMapping("check-code")
	public ResponseEntity<Boolean> checkConfirmationCode(@RequestParam("userId") Long user, @RequestBody String confirmationCode) {
		Optional<ExpiringConfirmationCode> code = codeRepository.findByUserId(user);
		boolean result = false;
		if (code.isPresent()) {
			ExpiringConfirmationCode c = code.get();
			if (c.isValid()) {
				result = c.getCode().equals(confirmationCode);
			}
		}
		return ResponseEntity.ok(result);
	}
	@PostMapping("check-temp")
	public ResponseEntity<Boolean> checkTempPassword(@RequestParam("userId") Long userId, @RequestBody String tempPassword) {
		User user = userService.findById(userId);
		boolean result = false;
		if (user.getTemporaryPassword().equals(tempPassword)) {
			result = true;
		}
		return ResponseEntity.ok(result);
	}
	
}
