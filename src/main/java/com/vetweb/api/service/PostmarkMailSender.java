package com.vetweb.api.service;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.vetweb.api.model.auth.PasswordRecovery;
import com.vetweb.api.model.auth.User;
import com.vetweb.api.service.auth.PasswordRecoveryService;
import com.vetweb.api.utils.EmailTemplateBuilder;
import com.vetweb.api.utils.EmailType;
import com.wildbit.java.postmark.Postmark;
import com.wildbit.java.postmark.client.ApiClient;
import com.wildbit.java.postmark.client.data.model.message.MessageResponse;
import com.wildbit.java.postmark.client.data.model.templates.TemplatedMessage;
import com.wildbit.java.postmark.client.exception.PostmarkException;

@Service
@PropertySource("classpath:email.properties")
public class PostmarkMailSender {
	
	@Autowired
	private PasswordRecoveryService passwordRecoveryService;
	
	@Value("${api.token}")
	private String API_TOKEN;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PostmarkMailSender.class);
	
	@Value("${email.from}")
	private String FROM;
	
	@Value("${email.template.forgot}")
	private String FORGOT_PASSWORD_TEMPLATE;

	@Value("${vetweb.domain}")
	private String DOMAIN;
	
	public void sendForgotPasswordEmail(User user) {
		TemplatedMessage message = new TemplatedMessage(FROM, user.getEmail());
		message.setTemplateAlias(this.FORGOT_PASSWORD_TEMPLATE);
		PasswordRecovery recoveryHash = this.passwordRecoveryService.generateRecoveryHash(user);
		Map<String, Object> propertiesTemplate = EmailTemplateBuilder.build(EmailType.FORGOT, user.getName(), "VetWeb", this.DOMAIN + "/auth/reset/" + recoveryHash.getHash(), "unknown", "unknown", "unknown", "unknown", "VetWeb", "unknown");
		message.setTemplateModel(propertiesTemplate);
		ApiClient apiClient = Postmark.getApiClient(API_TOKEN);
		try {
			MessageResponse messageResponse = apiClient.deliverMessageWithTemplate(message);
			LOGGER.info(String.format("Reset password email sent to user %s at %s", user.getName(), messageResponse.getSubmittedAt().toString()));
		} catch (PostmarkException postmarkException) {
			LOGGER.error("Library error " + postmarkException.getMessage());
			throw new RuntimeException("Cannot send email, receiver domain should be accountfy");
		} catch (IOException ioException) {
			LOGGER.error("Generic error " + ioException.getMessage());
		}
	}
	
	
}
