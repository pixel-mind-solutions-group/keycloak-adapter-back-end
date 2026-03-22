package com.pixelmind.keycloak_adapter.dto.user.permission;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PermissionRequest {

    private String realmName;
    private String username;

    // Roles assignment
    private String clientId;          // e.g. "my-app-client"
    private List<String> permissions = new ArrayList<>();   // e.g. ["ADMIN", "MANAGER"]
}
