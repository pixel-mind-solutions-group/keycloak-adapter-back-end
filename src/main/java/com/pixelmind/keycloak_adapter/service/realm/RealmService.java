package com.pixelmind.keycloak_adapter.service.realm;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.realm.RealmRequestDTO;
import com.pixelmind.keycloak_adapter.enums.PersistType;

public interface RealmService {

    CommonResponseDTO realm(PersistType persistType, RealmRequestDTO realmRequest);
}
