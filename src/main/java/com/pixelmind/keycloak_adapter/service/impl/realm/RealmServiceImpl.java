package com.pixelmind.keycloak_adapter.service.impl.realm;

import com.pixelmind.keycloak_adapter.constant.CommonConstant;
import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.realm.RealmRequestDTO;
import com.pixelmind.keycloak_adapter.enums.PersistType;
import com.pixelmind.keycloak_adapter.exception.BaseException;
import com.pixelmind.keycloak_adapter.service.realm.RealmService;
import jakarta.ws.rs.ClientErrorException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealmServiceImpl implements RealmService {

    private final Keycloak keycloak;

    @Override
    public CommonResponseDTO realm(PersistType persistType,
                                   RealmRequestDTO realmRequest) {

        String message = null;
        RealmRepresentation realmRepresentation;

        try {
            switch (persistType) {

                case CREATE: {

                    if (isRealmExists(realmRequest.getRealmName())) {
                        return new CommonResponseDTO(
                                HttpStatus.CONFLICT.value(),
                                null,
                                "Realm already exists: " + realmRequest.getRealmName()
                        );
                    }

                    // Create new realm object
                    realmRepresentation = new RealmRepresentation();

                    realmRepresentation.setRealm(realmRequest.getRealmName());
                    realmRepresentation.setDisplayName(realmRequest.getDisplayName());
                    realmRepresentation.setEnabled(realmRequest.getEnabled());

                    keycloak.realms().create(realmRepresentation);
                    message = "Realm created: " + realmRequest.getRealmName();
                    break;

                }
                case UPDATE: {

                    if (!isRealmExists(realmRequest.getId())) {
                        return new CommonResponseDTO(
                                HttpStatus.CONFLICT.value(),
                                null,
                                "Realm is not exists: " + realmRequest.getId()
                        );
                    }

                    // Get the existing realm
                    RealmResource realmResource = keycloak.realm(realmRequest.getId());

                    // Fetch current representation
                    realmRepresentation = realmResource.toRepresentation();

                    realmRepresentation.setDisplayName(realmRequest.getDisplayName());
                    realmRepresentation.setRealm(realmRequest.getRealmName());
                    realmRepresentation.setEnabled(realmRequest.getEnabled());

                    realmResource.update(realmRepresentation);
                    message = "Realm updated: " + realmRequest.getRealmName();
                    break;
                }
                case DELETE: {

                    // Prevent deleting master realm
                    if (!isRealmExists(realmRequest.getId())) {
                        return new CommonResponseDTO(
                                HttpStatus.CONFLICT.value(),
                                null,
                                "Realm is not exists: " + realmRequest.getId()
                        );

                    } else if (realmRequest.getId().equalsIgnoreCase(CommonConstant.MASTER_REALM_NAME)) {
                        return new CommonResponseDTO(
                                HttpStatus.FORBIDDEN.value(),
                                null,
                                "Cannot delete the master realm"
                        );
                    }

                    // Delete the realm
                    keycloak.realms().realm(realmRequest.getId()).remove();
                    message = "Realm deleted: " + realmRequest.getId();
                }
            }

        } catch (ClientErrorException e) {
            throw new BaseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Keycloak client error: " + e.getMessage()
            );

        } catch (Exception e) {
            throw new BaseException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal server error: " + e.getMessage()
            );
        }

        return new CommonResponseDTO(
                HttpStatus.OK.value(),
                null,
                message
        );
    }

    private boolean isRealmExists(String realmName) {
        return keycloak.realms().findAll()
                .stream()
                .anyMatch(r -> r.getRealm().equalsIgnoreCase(realmName));
    }
}
