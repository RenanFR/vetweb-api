package com.vetweb.api.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.vetweb.api.resources.AccountResource;
import com.vetweb.api.service.PostmarkMailSender;
import com.vetweb.api.service.auth.PasswordRecoveryService;
import com.vetweb.api.service.auth.UserService;

import io.micrometer.core.instrument.MeterRegistry;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {
		AccountResource.class
})
@ActiveProfiles("test")
public class UserControllerTest {
	
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private PostmarkMailSender sender;
	
	@MockBean
	private PasswordRecoveryService passwordRecoveryService;
	
	@MockBean
	private MeterRegistry registry;
	
	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void shouldLogin() throws Exception {
		given(userService.userExists("spring@email.com")).willReturn(true);
		boolean result = Boolean.parseBoolean(this.mvc
			.perform(get("/account/exists/spring@email.com"))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString());
		assertTrue(result);
	}

}
