package com.example.oauth2core.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FormUser {
    private String username;
    private String password;
}
