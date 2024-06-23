package com.oauth2.authorization.dto.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private static final String SUCCESS_MESSAGE = "SUCCESS";

    private int statusCode;

    private T message;

    private String requestTraceId;

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(200, data);
    }

    public static ApiResponse<?> of() {
        return new ApiResponse<>(200, SUCCESS_MESSAGE);
    }

    private ApiResponse(int statusCode, T message) {
        this.statusCode = statusCode;
        this.message = message;
        this.requestTraceId = "";
    }

}
