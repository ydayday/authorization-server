package com.oauth2.authorization.jpa.repository.querydsl;


import com.oauth2.authorization.dto.AuthorizationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomAuthRepository {
    Page<AuthorizationDto.Response> findBySearchCondition(AuthorizationDto.SearchCondition searchCondition, Pageable pageable);
}
