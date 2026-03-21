package com.pixelmind.keycloak_adapter.service.user;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.user.UserRequestDTO;
import com.pixelmind.keycloak_adapter.dto.user.credential.CredentialRequestDTO;

public interface UserService {

    CommonResponseDTO createUser(String realmName, UserRequestDTO user);

    CommonResponseDTO updateUser(String realmName, String userId, UserRequestDTO user);

    CommonResponseDTO updateCredential(String realmName, String userId, CredentialRequestDTO credentialRequest);

    CommonResponseDTO deleteCredential(String realmName, String userId);
}
