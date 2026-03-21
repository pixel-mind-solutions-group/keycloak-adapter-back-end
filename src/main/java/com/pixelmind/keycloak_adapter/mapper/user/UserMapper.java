package com.pixelmind.keycloak_adapter.mapper.user;

import com.pixelmind.keycloak_adapter.dto.user.UserRequestDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserRepresentation toUserRepresentation(UserRepresentation user, UserRequestDTO userRequest) {

        // ── Step 1: Build user representation ────────────────────
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEnabled(userRequest.isEnabled());
        user.setEmailVerified(userRequest.isEmailVerified());

        return user;
    }
}
