package com.pixelmind.keycloak_adapter.service.user;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.user.UserRequestDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<CommonResponseDTO> createRole(UserRequestDTO user);
}
