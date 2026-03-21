package com.pixelmind.keycloak_adapter.controller.auth;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.auth.TokenRequestDTO;
import com.pixelmind.keycloak_adapter.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/keycloak-adapter/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/token")
    public ResponseEntity<CommonResponseDTO> getToken(@RequestBody TokenRequestDTO tokenRequest) {
        return ResponseEntity.ok(authService.getToken(tokenRequest));
    }
}
