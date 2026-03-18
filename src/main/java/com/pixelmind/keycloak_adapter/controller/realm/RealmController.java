package com.pixelmind.keycloak_adapter.controller.realm;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.realm.RealmRequestDTO;
import com.pixelmind.keycloak_adapter.enums.PersistType;
import com.pixelmind.keycloak_adapter.service.realm.RealmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/keycloak-adapter/realm")
public class RealmController {

    private final RealmService realmService;

    @PostMapping(value = "")
    public ResponseEntity<CommonResponseDTO> realm(@RequestParam(value = "persistType") PersistType persistType,
                                                   @RequestBody RealmRequestDTO realmRequest) {

        return ResponseEntity.ok(realmService.realm(persistType, realmRequest));
    }
}
