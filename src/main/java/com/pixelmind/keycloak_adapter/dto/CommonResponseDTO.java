package com.pixelmind.keycloak_adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommonResponseDTO {
    private Integer status;
    private Object data;
    private String message;
}
