package com.pixelmind.keycloak_adapter.controller.role;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.role.RoleRequestDTO;
import com.pixelmind.keycloak_adapter.dto.user.UserRequestDTO;
import com.pixelmind.keycloak_adapter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/keycloak-adapter/role")
public class RoleController {

    private final UserService userService;

    @PostMapping(value = "/create")
    public ResponseEntity<CommonResponseDTO> createRole(@RequestBody RoleRequestDTO role) {
        return null;
//        return userService.createRole(role);
    }
}
