package com.oauth2.authorization.config.filter;

import com.oauth2.authorization.config.constant.SecurityPermitAll;
import com.oauth2.authorization.exception.ErrorCode;
import com.oauth2.authorization.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationRsaPublicKeyFilter extends OncePerRequestFilter {

	private final JwtDecoder jwtDecoder;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (!hasJWT(request)) {
			if (Arrays.asList(SecurityPermitAll.LIST.getValue()).indexOf(request.getRequestURI()) > 0) {
				Authentication authentication = new AnonymousAuthenticationToken(ALREADY_FILTERED_SUFFIX, filterChain, List.of(new OAuth2UserAuthority(Map.of("hello", "world"))));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				return;
			}
			throw new GlobalException(ErrorCode.INVALID_ACCESS_TOKEN);
		}

		if (jwtDecoder != null) {
			Jwt jwt;

			try {
				jwt = jwtDecoder.decode(getToken(request));
			} catch (JwtException ex) {
				throw new GlobalException(ErrorCode.INVALID_UNSECURED, ex);
			}

			String username = jwt.getClaimAsString("sub");
			if (username != null) {
				UserDetails user = User.builder().username(username).password("").authorities("ROLE_USER").build();
				Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

	protected String getToken(HttpServletRequest request) {
		return request.getHeader("Authorization").replace("Bearer ", "");
	}

	protected boolean hasJWT(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (!StringUtils.hasText(header)) {
			return false;
		}

		return header.startsWith("Bearer ");
	}

}
