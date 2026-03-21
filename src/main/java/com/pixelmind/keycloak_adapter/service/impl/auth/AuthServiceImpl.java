package com.pixelmind.keycloak_adapter.service.impl.auth;

import com.pixelmind.keycloak_adapter.constant.TokenConstant;
import com.pixelmind.keycloak_adapter.dto.CommonResponseDTO;
import com.pixelmind.keycloak_adapter.dto.auth.TokenRequestDTO;
import com.pixelmind.keycloak_adapter.exception.BaseException;
import com.pixelmind.keycloak_adapter.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RestClient restClient;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Override
    public CommonResponseDTO getToken(TokenRequestDTO tokenRequest) {

        // ── Build token URL ───────────────────────────────────────
        String tokenUrl = serverUrl + "/realms/"
                + tokenRequest.getRealmName()
                + "/protocol/openid-connect/token";

        // ── Build form body manually ──────────────────────────────
        String formBody = "grant_type=password"
                + "&client_id=" + tokenRequest.getClientId()
                + "&client_secret=" + tokenRequest.getClientSecret()
                + "&username=" + tokenRequest.getUsername()
                + "&password=" + tokenRequest.getPassword();

        try {
            Map response = restClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formBody)
                    .retrieve()
                    .body(Map.class);

            // Extract token data
            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("accessToken", response.get(TokenConstant.ACESS_TOKEN));
            tokenData.put("refreshToken", response.get(TokenConstant.REFRESH_TOKEN));
            tokenData.put("expiresIn", response.get(TokenConstant.EXPIRES_IN));
            tokenData.put("tokenType", response.get(TokenConstant.TOKEN_TYPE));

            return new CommonResponseDTO(
                    HttpStatus.OK.value(),
                    tokenData,
                    "Token generated successfully"
            );

        } catch (Exception e) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "Authentication failed: " + e.getMessage());
        }
    }
}
