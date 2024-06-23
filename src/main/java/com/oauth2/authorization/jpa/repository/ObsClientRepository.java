package com.oauth2.authorization.jpa.repository;


import com.oauth2.authorization.jpa.entity.ObsClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObsClientRepository extends JpaRepository<ObsClient, String> {

    Optional<ObsClient> findByClientId(String clientId);

    Optional<ObsClient> findByClientIdAndDeleteYnEquals(String clientId, String deleteYn);

    List<ObsClient> findAllByDeleteYnEquals(String deleteYn);

}
