package com.example.oauth2core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class TokenApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String accessToken;
    public String clientId;
    private LocalDateTime createdAt;


    public static final class TokenAppBuilder {
        public String accessToken;
        public String clientId;
        private Long id;
        private LocalDateTime createdAt;

        private TokenAppBuilder() {
        }

        public static TokenAppBuilder aTokenApp() {
            return new TokenAppBuilder();
        }

        public TokenAppBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TokenAppBuilder withAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public TokenAppBuilder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public TokenAppBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TokenApp build() {
            TokenApp tokenApp = new TokenApp();
            tokenApp.setId(id);
            tokenApp.setAccessToken(accessToken);
            tokenApp.setClientId(clientId);
            tokenApp.setCreatedAt(createdAt);
            return tokenApp;
        }
    }
}
