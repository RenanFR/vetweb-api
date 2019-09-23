package com.vetweb.api.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vetweb.api.model.PetOwner;
import com.vetweb.api.persist.PetOwnerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PetOwnerTest {
	
	@Autowired
	private PetOwnerRepository repository;
	
	@Test
	@Rollback(true)
	public void shouldCreatePetOwner() {
		assertEquals((long)1, (long)repository.create(new PetOwner.Builder().buildForTest()));
	}

}
