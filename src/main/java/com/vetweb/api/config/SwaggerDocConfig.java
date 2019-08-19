package com.vetweb.api.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@PropertySource("classpath:vetweb.properties")
public class SwaggerDocConfig {
	
	@Value("${api.title}")
	private String title;
	
	@Value("${api.description}")
	private String description;
	
	@Value("${api.version}")
	private String version;
	
	@Value("${api.termsOfServiceUrl}")
	private String termsOfServiceUrl;
	
	@Value("${api.contact.name}")
	private String contactName;
	
	@Value("${api.contact.url}")
	private String contactUrl;
	
	@Value("${api.contact.email}")
	private String contactEmail;
	
	@Value("${api.license}")
	private String license;
	
	@Value("${api.licenseUrl}")
	private String licenseUrl;
	
	private static final String HEADER = "Authorization";
	
	@Bean
	public Docket api() {
		Parameter parameter = new ParameterBuilder()
				.name(HEADER)
				.description("Header for Bearer JWT Token authorization")
				.modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		return new Docket(DocumentationType.SWAGGER_2)
				.globalOperationParameters(Arrays.asList(parameter))
//				.securityContexts(Lists.newArrayList(securityContext()))
//				.securitySchemes(Lists.newArrayList(apiKey()))
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfo(title, description, version, termsOfServiceUrl, new Contact(contactName, contactUrl, contactEmail), license, licenseUrl, Collections.emptyList());
	}
	
//	private ApiKey apiKey() {
//		return new ApiKey("JWT Token", HEADER, "header");
//	}
//	
//	private List<SecurityReference> authConfig() {
//		AuthorizationScope scope = new AuthorizationScope("global", "Access to API operations");
//		return Lists.newArrayList(new SecurityReference("Bearer JWT", new AuthorizationScope[] {scope}));
//	}
//	
//	private SecurityContext securityContext() {
//		return SecurityContext
//				.builder()
//				.securityReferences(authConfig())
//				.forPaths(Predicates.not(PathSelectors.regex("/account.*")))
//				.build();
//	}

}
