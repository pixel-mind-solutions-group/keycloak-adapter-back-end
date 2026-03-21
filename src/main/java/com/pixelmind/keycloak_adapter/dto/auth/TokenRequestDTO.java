package com.pixelmind.keycloak_adapter.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDTO {
    private String realmName;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
}
