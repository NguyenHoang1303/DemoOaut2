package com.example.oauth2core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.oauth2core.dto.Credential;
import com.example.oauth2core.entity.AuthorCode;
import com.example.oauth2core.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Log4j2
public class JwtHandler {

    public Credential createCredential(AuthorCode authorCode, HttpServletRequest request) {

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create().withSubject(authorCode.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 24 * 360 * 100))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("scope", authorCode.getScope())
                .sign(algorithm);
        String refresh_token = JWT.create().withSubject(authorCode.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 24 * 360 * 100))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        return new Credential(access_token, "Bearer", System.currentTimeMillis() + 10 * 24 * 360 * 100, refresh_token);
    }


    public User decodeToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorization = request.getHeader(AUTHORIZATION);
        User user = new User();
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring("Bearer ".length());
                log.info(token);
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                String scope = decodedJWT.getClaim("scope").asString();
                user.setUsername(username);
                user.setScope(scope);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                HashMap<String, String> errors = new HashMap<>();
                errors.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
        }
        return user;
    }
}
