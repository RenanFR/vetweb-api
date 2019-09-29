package com.vetweb.api.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.vetweb.api.model.auth.Profile;
import com.vetweb.api.model.auth.User;
import com.vetweb.api.service.auth.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void checkInjection() {
		assertNotNull(userService);
	}
	
	@Test
	public void testSignUp() {
		User user = userService.findByName("spring");
		if (user == null) {
			user = User
					.builder()
					.name("spring")
					.email("spring@email.com")
					.password("password")
					.inclusionDate(LocalDate.now())
					.profiles(List.of(Profile.builder().role("PET_OWNER").build()))
					.using2FA(true)
					.socialLogin(false)
					.build();
			userService.signUp(user);
		}
		assertTrue(passwordEncoder.matches("password", user.getPassword()));
	}

}
