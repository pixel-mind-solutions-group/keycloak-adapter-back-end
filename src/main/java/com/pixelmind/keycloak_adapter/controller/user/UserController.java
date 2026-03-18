package com.pixelmind.keycloak_adapter.controller.user;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.user.UserRequestDTO;
import com.pixelmind.keycloak_adapter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/keycloak-adapter/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/create")
    public ResponseEntity<CommonResponseDTO> createUser(@RequestBody UserRequestDTO user) {
        return userService.createRole(user);
    }
}
