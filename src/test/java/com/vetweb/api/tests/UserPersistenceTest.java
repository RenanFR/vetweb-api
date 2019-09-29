package com.vetweb.api.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vetweb.api.model.auth.Profile;
import com.vetweb.api.model.auth.User;
import com.vetweb.api.persist.auth.ProfileRepository;
import com.vetweb.api.persist.auth.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserPersistenceTest {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProfileRepository profileRepository;
	
	@Test
	public void checkDependencies() {
		assertNotNull(dataSource);
		assertNotNull(jdbcTemplate);
		assertNotNull(entityManager);
		assertNotNull(userRepository);
		assertNotNull(profileRepository);
	}
	
	@Test
	public void testCreateUser() {
		Profile profile = Profile.builder().role("role").build();
		Profile savedProfile = profileRepository.save(profile);
		assertEquals(profile.getRole(), savedProfile.getRole());
		User user = User
				.builder()
				.name("user")
				.email("user@email com")
				.password("password")
				.profiles(List.of(savedProfile))
				.build();
		User saved = userRepository.save(user);
		assertEquals(2, saved.getId(), 0);
	}
	
	@Test
	public void testFindUser() {
		assertTrue(userRepository.findByName("Renan Rodrigues").isPresent());
	}
	
	@Test
	public void testFindByEmail() {
		assertTrue(userRepository.findByEmail("renan.rodrigues@accountfy.com").isPresent());
	}

}
