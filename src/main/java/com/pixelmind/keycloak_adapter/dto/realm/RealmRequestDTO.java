package com.pixelmind.keycloak_adapter.dto.realm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealmRequestDTO {
    private String id;
    private String realmName;
    private String displayName;
    private Boolean enabled;
}
