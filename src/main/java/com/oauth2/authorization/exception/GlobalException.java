package com.oauth2.authorization.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2Error;

@Slf4j
@Getter
public class GlobalException extends RuntimeException {

    private final HttpStatus status;

    private final ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
        this.errorCode = errorCode;
    }

    public GlobalException(ErrorCode errorCode, Throwable ex) {
        this(errorCode);
        printErrorLog(ex);
    }

    public GlobalException(OAuth2Error oAuth2Error) {
        super(oAuth2Error.getErrorCode());
        this.status = ErrorCode.INVALID_CLIENT_INFO.getStatus();
        this.errorCode = ErrorCode.INVALID_CLIENT_INFO;

        log.error("OAuth2Error : {}", oAuth2Error);
    }

    private static void printErrorLog(Throwable ex) {
        log.error("==================== Exception ====================");
        log.error("Target : {}", ex.getStackTrace());
        log.error("Message : {}", ex.getMessage());
        log.error("==================== Exception ====================");
    }


}
