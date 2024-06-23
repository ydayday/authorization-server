package com.oauth2.authorization.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private int statusCode;

    private String message;

    private String requestTraceId;

    public static ErrorResponse of(GlobalException ex) {
        return ErrorResponse.builder()
                .statusCode(ex.getErrorCode().getStatus().value())
                .message(ex.getErrorCode().getMessage())
                .requestTraceId("")
            .build();
    }

    public static ErrorResponse of(GlobalException ex, String traceId) {
        return ErrorResponse.builder()
                .statusCode(ex.getErrorCode().getStatus().value())
                .message(ex.getErrorCode().getMessage())
                .requestTraceId("")
            .build();
    }

    public static ErrorResponse of(HttpStatus status, String message) {
        return ErrorResponse.builder()
                .statusCode(status.value())
                .message(message)
                .requestTraceId("")
            .build();
    }

    public static ErrorResponse of(HttpStatus status, String message, String traceId) {
        return ErrorResponse.builder()
                .statusCode(status.value())
                .message(message)
                .requestTraceId("")
            .build();
    }

}
