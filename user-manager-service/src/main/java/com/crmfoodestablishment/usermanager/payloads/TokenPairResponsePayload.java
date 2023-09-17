package com.crmfoodestablishment.usermanager.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenPairResponsePayload {

    private String accessToken;

    private String refreshToken;
}
