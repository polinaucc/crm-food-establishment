package com.crmfoodestablishment.usermanager.payloads;

import lombok.Data;

@Data
public class RefreshPayload {

    private String refreshToken;

    private String email;
}
