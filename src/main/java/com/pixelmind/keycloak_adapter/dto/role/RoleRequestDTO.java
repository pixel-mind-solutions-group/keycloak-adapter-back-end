package com.pixelmind.keycloak_adapter.dto.role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequestDTO {
    private String roleScope; // realm | client
    private String name;
    private String description;
}
