package com.pixelmind.keycloak_adapter.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String appKey;
    private String appRole;
}
