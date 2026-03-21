package com.pixelmind.keycloak_adapter.service.impl.user;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.user.UserRequestDTO;
import com.pixelmind.keycloak_adapter.dto.user.credential.CredentialRequestDTO;
import com.pixelmind.keycloak_adapter.exception.BaseException;
import com.pixelmind.keycloak_adapter.mapper.user.UserMapper;
import com.pixelmind.keycloak_adapter.service.user.UserService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;
    private final UserMapper userMapper;

    @Override
    public CommonResponseDTO createUser(String realmName,
                                        UserRequestDTO userRequest) {

        UserRepresentation userRepresentation = userMapper.toUserRepresentation(new UserRepresentation(), userRequest);

        // ── Step 3: Create user ───────────────────────────────────
        Response response = keycloak
                .realm(realmName)
                .users()
                .create(userRepresentation);

        if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            throw new BaseException(HttpStatus.CONFLICT.value(), "User already exists: " + userRequest.getUsername());
        }

        if (response.getStatus() != HttpStatus.CREATED.value()) {
            throw new BaseException(response.getStatus(), "Failed to create user");
        }

        // ── Step 4: Extract new userId from Location header ───────
        String locationHeader = response.getHeaderString("Location");
        String userId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

        // ── Step 5: Assign client roles if provided ───────────────
        if (userRequest.getClientId() != null &&
                userRequest.getPermissions() != null &&
                !userRequest.getPermissions().isEmpty()) {

            try {
                assignClientRoles(realmName, userId, userRequest.getClientId(), userRequest.getPermissions());

            } catch (NotFoundException e) {

                // ── Rollback: delete user if role assignment failed ───────
                rollbackUserCreation(realmName, userId);

                throw new BaseException(HttpStatus.NOT_FOUND.value(), e.getMessage());

            } catch (Exception e) {

                // ── Rollback: delete user if anything failed ──────────────
                rollbackUserCreation(realmName, userId);

                throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error: " + e.getMessage());
            }
        }

        return new CommonResponseDTO(
                HttpStatus.CREATED.value(),
                "User created and roles assigned successfully",
                "USER ID: " + userId
        );
    }

    @Override
    public CommonResponseDTO updateUser(String realmName,
                                        String userId,
                                        UserRequestDTO userRequest) {

        // ── Step 1: Get existing user ─────────────────────────────
        UserResource userResource = keycloak
                .realm(realmName)
                .users()
                .get(userId);

        UserRepresentation existingUser = userResource.toRepresentation();

        // ── Step 3: Apply user update ─────────────────────────────
        userResource.update(userMapper.toUserRepresentation(existingUser, userRequest));

        // ── Step 4: Update client roles if provided ───────────────
        if (userRequest.getClientId() != null &&
                !userRequest.getPermissions().isEmpty()) {

            try {
                assignClientRoles(realmName, userId, userRequest.getClientId(), userRequest.getPermissions());

            } catch (NotFoundException e) {
                throw new BaseException(HttpStatus.NOT_FOUND.value(), e.getMessage());

            } catch (Exception e) {
                throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error: " + e.getMessage());
            }
        }

        return new CommonResponseDTO(
                HttpStatus.OK.value(),
                "User updated successfully: " + userId,
                null
        );
    }

    // ── Rollback Helper ───────────────────────────────────────────────────────────
    private void rollbackUserCreation(String realmName, String userId) {
        try {
            if (userId != null) {
                keycloak.realm(realmName).users().get(userId).remove();
            }
        } catch (Exception rollbackEx) {
            // Log rollback failure — needs manual cleanup
            log.error("ROLLBACK FAILED — manually delete userId: {} in realm: {}", userId, realmName);
            throw new BaseException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "ROLLBACK FAILED — manually delete userId: " + userId + " in realm: " + realmName);
        }
    }

    // ── Role Assignment Helper ────────────────────────────────────────────────────
    private void assignClientRoles(
            String realmName,
            String userId,
            String clientId,
            List<String> permissions
    ) {

        // Step 1: Find the internal client UUID by clientId name
        ClientRepresentation client = keycloak
                .realm(realmName)
                .clients()
                .findByClientId(clientId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Client not found: " + clientId));

        String clientUUID = client.getId();

        // Step 2: Fetch all available roles for that client
        List<RoleRepresentation> availableRoles = keycloak
                .realm(realmName)
                .clients()
                .get(clientUUID)
                .roles()
                .list();

        // Step 3: Filter only the roles that were requested
        List<RoleRepresentation> rolesToAssign = availableRoles.stream()
                .filter(role -> permissions.contains(role.getName()))
                .toList();

        if (rolesToAssign.isEmpty()) {
            throw new NotFoundException("None of the provided roles found in client: " + clientId);
        }

        RoleMappingResource roleMappingResource = keycloak
                .realm(realmName)
                .users()
                .get(userId)
                .roles();

        List<RoleRepresentation> existingRoles = roleMappingResource
                .clientLevel(clientUUID)
                .listAll();

        if (!existingRoles.isEmpty()) {
            roleMappingResource
                    .clientLevel(clientUUID)
                    .remove(existingRoles);
        }

        // Step 4: Assign filtered roles to the user
        keycloak.realm(realmName)
                .users()
                .get(userId)
                .roles()
                .clientLevel(clientUUID)
                .add(rolesToAssign);
    }

    @Override
    public CommonResponseDTO updateCredential(
            String realmName,
            String userId,
            CredentialRequestDTO credentialRequest) {

        // ── Step 1: Build new credential ──────────────────────────
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(credentialRequest.getPassword());
        credential.setTemporary(credentialRequest.isTemporary());

        try {
            // ── Step 2: Reset password for the user ───────────────────
            keycloak.realm(realmName)
                    .users()
                    .get(userId)
                    .resetPassword(credential);

            String message = credentialRequest.isTemporary()
                    ? "Temporary credential set — user must reset on first login"
                    : "Credential updated successfully for userId: " + userId;

            return new CommonResponseDTO(
                    HttpStatus.OK.value(),
                    message, null
            );

        } catch (NotFoundException e) {
            return new CommonResponseDTO(404, "User or Realm not found", null);
        } catch (Exception e) {
            return new CommonResponseDTO(500, "Unexpected error: " + e.getMessage(), null);
        }
    }

    @Override
    public CommonResponseDTO deleteCredential(String realmName,
                                              String userId) {
        try {
            // ── Step 1: Get user resource ─────────────────────────────
            UserResource userResource = keycloak
                    .realm(realmName)
                    .users()
                    .get(userId);

            // ── Step 2: Check credential exists ───────────────────────
            List<CredentialRepresentation> credentials = userResource.credentials();
            if (credentials.isEmpty()) {
                throw new BaseException(HttpStatus.NOT_FOUND.value(), "No credentials found for userId: " + userId);
            }

            // ── Step 3: Loop and remove each credential ────────────────
            credentials.forEach(c -> userResource.removeCredential(c.getId()));

            return new CommonResponseDTO(
                    HttpStatus.OK.value(),
                    "Credentials deleted successfully",
                    null
            );

        } catch (BaseException e) {
            throw new BaseException(e.getErrorCode(), e.getErrorDescription());

        } catch (NotFoundException e) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "User or Realm not found");

        } catch (Exception e) {
            throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error: " + e.getMessage());
        }
    }
}
