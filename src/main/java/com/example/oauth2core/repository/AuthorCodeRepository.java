package com.example.oauth2core.repository;

import com.example.oauth2core.entity.AuthorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorCodeRepository extends JpaRepository<AuthorCode,Long> {
    AuthorCode findAuthorCodeByClientId(String clientId);
    AuthorCode findAuthorCodeByAuthorizationCode(String code);
}
