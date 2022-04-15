package com.example.oauth2core.repository;

import com.example.oauth2core.entity.TokenApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenAppRepository extends JpaRepository<TokenApp, Long> {
    TokenApp findTokenAppByAccessToken(String token);
}
