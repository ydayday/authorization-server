package com.oauth2.authorization.jpa.entity;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "OAUTH2_AUTHORIZATION")
@Getter
@ToString
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Authorization {

    @Id @Column
    private String id;

    private String registeredClientId;

    private String principalName;

    private String authorizationGrantType;

    @Column(length = 4000)
    private String attributes;

    @Column(length = 500)
    private String state;

    @Column(length = 4000)
    private String authorizationCodeValue;

    private String authorizationCodeIssuedAt;

    private String authorizationCodeExpiresAt;

    private String authorizationCodeMetadata;

    @Column(length = 4000)
    private String accessTokenValue;

    private LocalDateTime accessTokenIssuedAt;

    private LocalDateTime accessTokenExpiresAt;

    @Column(length = 2000)
    private String accessTokenMetadata;

    private String accessTokenType;

    @Column(length = 1000)
    private String accessTokenScopes;

    @Column(length = 4000)
    private String refreshTokenValue;

    private String refreshTokenIssuedAt;

    private String refreshTokenExpiresAt;

    @Column(length = 2000)
    private String refreshTokenMetadata;

    @Column(length = 4000)
    private String oidcIdTokenValue;

    private String oidcIdTokenIssuedAt;

    private String oidcIdTokenExpiresAt;

    @Column(length = 2000)
    private String oidcIdTokenMetadata;

}
