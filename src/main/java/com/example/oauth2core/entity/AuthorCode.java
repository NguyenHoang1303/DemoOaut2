package com.example.oauth2core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table
public class AuthorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientId;
    private String authorizationCode;
    private String scope;
    private String username;
    private LocalDateTime expires_in;
    private LocalDateTime createdAt;

    public AuthorCode(String clientId, String authorizationCode, String scope, String username) {
        this.clientId = clientId;
        this.authorizationCode = authorizationCode;
        this.scope = scope;
        this.username = username;
        this.expires_in = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
}
