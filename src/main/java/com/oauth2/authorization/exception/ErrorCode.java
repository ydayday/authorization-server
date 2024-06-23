package com.oauth2.authorization.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "해당 토큰은 만료되었거나 유효하지 않습니다."),
    INVALID_UNSECURED(HttpStatus.FORBIDDEN, "INVALID_UNSECURED[JWS/JWE]"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    NOT_FOUND_CLIENT(HttpStatus.NOT_FOUND, "클라이언트가 존재하지 않습니다."),
    ALREADY_OBS_CLIENT(HttpStatus.INTERNAL_SERVER_ERROR, "이미 존재하는 클라이언트입니다."),
    PEM_FILE_READ_FAIL(HttpStatus.NOT_FOUND, "PEM_FILE_READ_FAIL"),
    INVALID_CLIENT_INFO(HttpStatus.UNAUTHORIZED, "해당 클라이언트는 만료되었거나 유효하지 않습니다."),

    ;



    private final HttpStatus status;

    private final String message;

}
