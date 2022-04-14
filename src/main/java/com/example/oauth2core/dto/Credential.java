package com.example.oauth2core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

    public String access_token;
    public String token_type;
    public Long expires_in;
    public String refresh_token;
}
