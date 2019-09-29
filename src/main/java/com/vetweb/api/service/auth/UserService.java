package com.vetweb.api.service.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vetweb.api.model.auth.Profile;
import com.vetweb.api.model.auth.User;
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
	
	public String signUp(User user) {
		user.setProfiles(Arrays.asList(profileRepository.findById("PET_OWNER").get()));
		if (user.isUsing2FA()) {
			user.setTwoFASecret(Base32.random());
		}
		user.setInclusionDate(LocalDate.now());
		this.saveUser(user);
		try {
			return user.isUsing2FA() ? this.generateQRCode(user) : "User created successfully";
		} catch (UnsupportedEncodingException exception) {
			return "Error generating QR code for user, ask for support";
		}
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
	
}
