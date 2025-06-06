package com.service.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.service.SpringApplicationContext;
import com.service.constants.SecurityConstants;
import com.service.entity.UserEntity;
import com.service.repository.UserRepository;
import com.service.shared.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String authToken = request.getHeader(SecurityConstants.AUTH_HEADER);
			String userId = null;
			String token = null;
			JwtUtils jwtUtils = SpringApplicationContext.fetchBean("jwtUtils", JwtUtils.class);
			if (authToken != null && authToken.startsWith(SecurityConstants.TOKEN_TYPE)) {
				token = authToken.substring(7).trim();
				userId = jwtUtils.extractUserId(token);
			}
			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserEntity userEntity = SpringApplicationContext.fetchBean("userRepository", UserRepository.class)
						.findById(userId).orElseThrow();
				UserPrincipal principal = new UserPrincipal(userEntity);
				if (jwtUtils.validateToken(token, principal)) {
					SecurityContextHolder.getContext().setAuthentication(
							new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
				}
			}
			chain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("{\"error\": \"JWT Token has expired\"}");
		} catch (JwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("{\"error\": \"Invalid JWT Token\"}");
		}
	}
}