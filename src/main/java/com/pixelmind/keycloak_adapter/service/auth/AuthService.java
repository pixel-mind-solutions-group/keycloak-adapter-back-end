package com.pixelmind.keycloak_adapter.service.auth;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.auth.TokenRequestDTO;

public interface AuthService {

    CommonResponseDTO getToken(TokenRequestDTO tokenRequest);
}
