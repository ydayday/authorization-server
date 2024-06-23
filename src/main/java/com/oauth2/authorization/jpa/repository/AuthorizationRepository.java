package com.oauth2.authorization.jpa.repository;


import com.oauth2.authorization.jpa.entity.Authorization;
import com.oauth2.authorization.jpa.repository.querydsl.CustomAuthRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, String>, CustomAuthRepository {

    Optional<Authorization> findByPrincipalName(String clientId);

}
