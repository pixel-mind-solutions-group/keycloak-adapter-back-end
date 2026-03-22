package com.pixelmind.keycloak_adapter.controller.user;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.user.UserRequestDTO;
import com.pixelmind.keycloak_adapter.dto.user.credential.CredentialRequestDTO;
import com.pixelmind.keycloak_adapter.dto.user.permission.PermissionRequest;
import com.pixelmind.keycloak_adapter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/keycloak-adapter/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/create")
    public ResponseEntity<CommonResponseDTO> createUser(@RequestParam(value = "realmName") String realmName,
                                                        @RequestBody UserRequestDTO user) {
        return ResponseEntity.ok(userService.createUser(realmName, user));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<CommonResponseDTO> updateUser(@RequestParam(value = "realmName") String realmName,
                                                        @RequestParam(value = "userId") String userId,
                                                        @RequestBody UserRequestDTO user) {
        return ResponseEntity.ok(userService.updateUser(realmName, userId, user));
    }

    @PostMapping(value = "/assign-permissions")
    public ResponseEntity<CommonResponseDTO> assignClientRoles(@RequestBody PermissionRequest permissionRequest) {
        return ResponseEntity.ok(userService.assignClientRoles(permissionRequest));
    }

    @PutMapping(value = "/credential/reset")
    public ResponseEntity<CommonResponseDTO> updateCredential(@RequestParam(value = "realmName") String realmName,
                                                              @RequestParam(value = "userId") String userId,
                                                              @RequestBody CredentialRequestDTO credentialRequest) {
        return ResponseEntity.ok(userService.updateCredential(realmName, userId, credentialRequest));
    }

    @DeleteMapping(value = "/credential/delete")
    public ResponseEntity<CommonResponseDTO> deleteCredential(@RequestParam(value = "realmName") String realmName,
                                                              @RequestParam(value = "userId") String userId) {
        return ResponseEntity.ok(userService.deleteCredential(realmName, userId));
    }
}
