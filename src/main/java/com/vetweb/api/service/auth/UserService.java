package com.vetweb.api.service.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vetweb.api.model.auth.ExpiringConfirmationCode;
import com.vetweb.api.model.auth.PasswordRecovery;
import com.vetweb.api.model.auth.Profile;
import com.vetweb.api.model.auth.User;
import com.vetweb.api.persist.auth.ExpiringConfirmationCodeRepository;
import com.vetweb.api.persist.auth.ProfileRepository;
import com.vetweb.api.persist.auth.UserRepository;

@Service
@PropertySource("classpath:vetweb.properties")
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ExpiringConfirmationCodeRepository codeRepository;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private PasswordRecoveryService passwordRecoveryService;	
	
	@Value("${vetweb.domain}")
	private String DOMAIN;
	
	private static final String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
	
	private static String APP_NAME;

	@Value("${api.title}")
	public void setAppName(String appName) {
		APP_NAME = appName;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository
					.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not founded"));
	}
	
	public User findByName(String userName) {
		try {
			return userRepository.findByName(userName).get();
		} catch (NoSuchElementException exception) {
			return null;
		}
	}
	
	public User findByEmail(String email) {
		try {
			return userRepository.findByEmail(email).get();
		} catch (NoSuchElementException exception) {
			return null;
		}
	}
	
	private ExpiringConfirmationCode generateConfirmationCodeForUser(User user) {
		ExpiringConfirmationCode code = new ExpiringConfirmationCode();
		code.setCode(Base32.random());
		code.setIssuedAt(LocalDateTime.now());
		code.setExpiration(LocalDateTime.now().plusDays(1));
		code.setUser(user);
		return codeRepository.save(code); 
	}
	
	public String signUp(User user) {
		user.setProfiles(Arrays.asList(profileRepository.findById("PET_OWNER").get()));
		ExpiringConfirmationCode code = null;
		user.setInclusionDate(LocalDate.now());
		user.setEnabled(true);
		this.saveUser(user);
		if (user.isUsing2FA()) {
			user.setTwoFASecret(Base32.random());
		} else {
			code = this.generateConfirmationCodeForUser(user);
		}
		try {
			String qrCodeOrMessage = user.isUsing2FA() ? this.generateQRCode(user) : code.getCode();
			String invitationText = "Congratulations, you were invited to use VetWeb, use temporary password " + user.getTemporaryPassword() + ", " 
					+ (user.isUsing2FA()? "please scan the following QR Code with your phone and join us for the first time " : " please use the following code to complete your registration before it expires ");
			invitationText = invitationText.concat(qrCodeOrMessage);
			this.sendEmail(user.getEmail(), "VetWeb invitation", invitationText);
			return qrCodeOrMessage;
		} catch (UnsupportedEncodingException exception) {
			return "Error generating QR code for user, ask for support";
		}
	}
	
	public void forgotPassword(User user) {
		String emailText = null;
		if (!user.getTemporaryPassword().equals("NOT_SET") && user.hasConfirmationCode()) {
			if (user.hasValidCode()) {
				emailText = String.format("You still did not switch your temporary password (%s) for a new one and your confirmation code (%s) is still valid", user.getTemporaryPassword(), user.getCodesOfConfirmation().stream().filter(c -> c.isValid()).findFirst().get().getCode());
			} else {
				emailText = String.format("You still did not switch your temporary password (%s) for a new one but your confirmation code is not valid anymore, here's a new one (%s)", user.getTemporaryPassword(), this.generateConfirmationCodeForUser(user).getCode());
			}
		} else {
			PasswordRecovery recoveryHash = this.passwordRecoveryService.generateRecoveryHash(user);
			emailText = "You recently requested a new password, please use the following URL to complete the operation: " + this.DOMAIN + "/auth/reset/" + recoveryHash.getHash();
		}
		this.sendEmail(user.getEmail(), "Reset Password", emailText);
	}
	
	private void sendEmail(String to, String subject, String text) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(to);
		email.setSubject(subject); 
		email.setText(text);
		this.mailSender.send(email);
	}
	
	public List<User> getAll() {
		return userRepository.findAll();
	}
	
	public List<Profile> getAllProfiles() {
		return profileRepository.findAll();
	}
	
	public void saveProfile(Profile profile) {
		profileRepository.save(profile);
	}
	
	public boolean userExists(String user) {
		return userRepository.findByEmail(user).isPresent();
	}
	
	public void saveUser(User user) {
		if (user.getPassword() != null)
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}
	
	public Profile findProfileByDescription(String id) {
		return profileRepository.findById(id).get();
	}
	
	public void remove(User user) {
		userRepository.delete(user);
	}
	
	public String generateQRCode(User account) throws UnsupportedEncodingException {
		return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, account.getEmail(), account.getTwoFASecret(), APP_NAME), "UTF-8");
	}
	
	public List<User> findContactsFor(String user) {
		return userRepository.findByEmailNot(user);
	}
	
	public User findById(Long id) {
		return userRepository.findById(id).get();
	}
}
