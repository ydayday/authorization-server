package com.oauth2.authorization.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oauth2.authorization.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * com.oauth2.authorization
 * TokenEndpointSuccessHandler.java
 * </pre>
 *
 * @author : insung
 * @date : 2024. 3. 07.
 * @desc : 백오피스/도매사 토큰 발급요청 구분을 위한 SuccessHandler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenEndpointFailHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;



    private <T> void send(HttpServletResponse response, T body) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try{
            response.getWriter().write(objectMapper.writeValueAsString(body));
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.valueOf(HttpStatus.UNAUTHORIZED.value()), exception.getMessage());
        send(response, errorResponse);
    }

}
