package com.vetweb.api.tests;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.vetweb.api.model.PetOwner;
import com.vetweb.api.persist.PetOwnerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application-test.properties")
//@DataJpaTest
public class PetOwnerTest {
	
	@Autowired
//	@MockBean
	private PetOwnerRepository repository;
	
	@Test
	public void shouldCreatePetOwner() {
		assertNotEquals((long)1, (long)repository.create(new PetOwner.Builder().buildForTest()));
	}

}
