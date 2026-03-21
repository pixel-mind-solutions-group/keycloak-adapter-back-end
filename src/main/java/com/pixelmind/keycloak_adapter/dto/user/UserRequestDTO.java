package com.pixelmind.keycloak_adapter.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    // Role assignment
    private String clientId;          // e.g. "my-app-client"
    private List<String> permissions = new ArrayList<>();   // e.g. ["ADMIN", "MANAGER"]
}
