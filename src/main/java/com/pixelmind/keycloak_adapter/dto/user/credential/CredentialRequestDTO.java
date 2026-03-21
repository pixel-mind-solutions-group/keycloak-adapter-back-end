package com.pixelmind.keycloak_adapter.dto.user.credential;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialRequestDTO {
    private String password;
    private boolean temporary;   // true = force reset on first login
}
