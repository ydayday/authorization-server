package com.oauth2.authorization.config.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityPermitAll {

    LIST(new String[]{"/", "/api/health"});

    private final String[] value;
}
