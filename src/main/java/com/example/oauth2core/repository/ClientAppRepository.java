package com.example.oauth2core.repository;


import com.example.oauth2core.entity.ClientApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientAppRepository extends JpaRepository<ClientApp, Long> {

    ClientApp findClientAppByClientId(String clientId);
}
