package com.vetweb.api.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vetweb.api.service.auth.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter	{
	
	@Autowired
	private UserService userService;
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}

	//Include JWT generation from login request and common requests from a provided JWT
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic()
				.disable()
			.csrf()
				.disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/v2/api-docs").permitAll()
				.antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
				.antMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
				.antMatchers(HttpMethod.GET, "/swagger.json").permitAll()
				.antMatchers(HttpMethod.GET, "/webjars/**").permitAll()
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.antMatchers(HttpMethod.POST, "/account/signin").permitAll()
				.antMatchers(HttpMethod.GET, "/categories/test").permitAll()
				.antMatchers(HttpMethod.GET, "/account/exists/{user}").permitAll()
				.antMatchers(HttpMethod.GET, "/account/uses-tfa/{user}").permitAll()
				.antMatchers(HttpMethod.GET, "/account/uses-tfa/{user}").permitAll()
				.antMatchers(HttpMethod.GET, "/account/using-hash/{user}").permitAll()
				.antMatchers(HttpMethod.GET, "/account/has-valid-hash/{user}").permitAll()
				.antMatchers(HttpMethod.POST, "/account/forget").permitAll()
				.antMatchers(HttpMethod.PUT, "/account/update").permitAll()
				.antMatchers(HttpMethod.POST, "/account/google").permitAll()
				.anyRequest()
				.authenticated()
			.and()
				.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JWTAuthenticationVerifier(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
			.antMatchers(HttpMethod.POST, "/account/signup");
	}
	
	@Bean
	public PasswordEncoder encoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userService)
			.passwordEncoder(encoder());
	}

}
