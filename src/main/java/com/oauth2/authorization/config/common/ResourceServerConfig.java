package com.oauth2.authorization.config.common;

import com.oauth2.authorization.config.constant.SecurityPermitAll;
import com.oauth2.authorization.config.filter.ExceptionHandlerFilter;
import com.oauth2.authorization.config.filter.JwtAuthorizationRsaPublicKeyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class ResourceServerConfig {

    private final ExceptionHandlerFilter exceptionHandler;

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests(request -> request.antMatchers(String.valueOf(SecurityPermitAll.LIST)).permitAll().anyRequest().authenticated());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthorizationRsaPublicKeyFilter(null), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(exceptionHandler, JwtAuthorizationRsaPublicKeyFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthorizationRsaPublicKeyFilter jwtAuthorizationRsaPublicKeyFilter(JwtDecoder jwtDecoder) {
        return new JwtAuthorizationRsaPublicKeyFilter(jwtDecoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
