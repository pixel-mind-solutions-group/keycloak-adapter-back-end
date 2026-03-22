package com.pixelmind.keycloak_adapter.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean temporary;
    private boolean enabled;
    private boolean emailVerified;
}
