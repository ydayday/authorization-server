package com.oauth2.authorization.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oauth2.authorization.dto.common.OAuth2TokenRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

import static com.oauth2.authorization.utils.GlobalUtils.IS8601FormatConverting;

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
public class TokenEndpointSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AccessTokenAuthenticationToken authenticationToken = (OAuth2AccessTokenAuthenticationToken) authentication;
        OAuth2AccessToken accessToken = authenticationToken.getAccessToken();

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            if (true) {
                long expiresIn = calcExpiresIn(accessToken);
                String tokenType = accessToken.getTokenType().getValue();
                String scope = String.join(" ", accessToken.getScopes());

                OAuth2TokenRes.StandardFormat standardFormatRes = OAuth2TokenRes.StandardFormat.builder()
                        .accessToken(accessToken.getTokenValue())
                        .scope(scope)
                        .tokenType(tokenType)
                        .expiresIn(expiresIn)
                        .build();

                send(response, standardFormatRes);
                return;
            }
        }

        String expiresAt = IS8601FormatConverting( String.valueOf(accessToken.getExpiresAt()) );
        String issuedAt = IS8601FormatConverting( String.valueOf(accessToken.getIssuedAt()) );

        OAuth2TokenRes.CustomFormat customFormat = OAuth2TokenRes.CustomFormat.builder()
                .token(accessToken.getTokenValue())
                .iat(issuedAt)
                .exp(expiresAt)
            .build();

        send(response, customFormat);
    }
    
    /**
     * <pre>
     * 1. 개요 : OAuth2.0 표준 응답 형식의 날짜를 계산한다.
     * 2. 처리내용 : 만료 날짜를 계산하여 초로 변환한다. (ex:299초)
     * </pre>
     * @Method Name : calcExpiresIn
     * @date : 2024. 3. 07.
     * @author : insung
     * @history :
     * ----------------------------------------------------------------------------------
     * 변경일 작성자 변경내역
     * -------------- -------------- ----------------------------------------------------
     * 2024. 3. 07. insung 최초작성
     * ----------------------------------------------------------------------------------
     */
    private static long calcExpiresIn(OAuth2AccessToken accessToken) {
        long expiresAtEpochSecond = Objects.requireNonNull(accessToken.getExpiresAt()).getEpochSecond();
        long issuedAtEpochSecond = Objects.requireNonNull(accessToken.getIssuedAt()).getEpochSecond();
        return (expiresAtEpochSecond - issuedAtEpochSecond) - 1L;
    }

    private <T> void send(HttpServletResponse response, T body) {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try{
            response.getWriter().write(objectMapper.writeValueAsString(body));
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

}
