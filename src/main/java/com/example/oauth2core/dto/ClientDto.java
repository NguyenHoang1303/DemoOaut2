package com.example.oauth2core.dto;

import com.example.oauth2core.entity.ClientApp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private Long id;
    private String clientId;
    private String secretId;
    private String logo;
    private String rootUrl;
    private String username;
    private String description;
    private String appName;

    public ClientDto(ClientApp clientApp) {
        logo = clientApp.getLogo();
        id = clientApp.getId();
        clientId = clientApp.getClientId();
        username = clientApp.getUser().getUsername();
        appName = clientApp.getName();
    }
}
