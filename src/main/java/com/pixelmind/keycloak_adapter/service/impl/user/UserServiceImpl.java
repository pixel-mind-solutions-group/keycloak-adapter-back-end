package com.pixelmind.keycloak_adapter.service.impl.user;

import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.user.UserRequestDTO;
import com.pixelmind.keycloak_adapter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Override
    public ResponseEntity<CommonResponseDTO> createRole(UserRequestDTO user) {
        // requesrd user action, email verified, username, email, firstname, lastname
//        --------
//        password, password confirmation, temporary
        // roles mapping -> client roles | realm roles

        return null;
    }
}
