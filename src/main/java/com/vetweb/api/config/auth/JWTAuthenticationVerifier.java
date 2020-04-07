package com.vetweb.api.config.auth;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vetweb.api.pojo.ApiErrorDTO;

import io.jsonwebtoken.ExpiredJwtException;

//Verifies the token existence in a common request to authorize response
public class JWTAuthenticationVerifier extends GenericFilterBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			Authentication authentication = TokenService
					.getTokenFromRequest((HttpServletRequest) request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			ApiErrorDTO errorDTO = new ApiErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Your Token has expired", Collections.emptyList());
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(errorDTO.getStatus());
			httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(errorDTO));
		}
	}

}