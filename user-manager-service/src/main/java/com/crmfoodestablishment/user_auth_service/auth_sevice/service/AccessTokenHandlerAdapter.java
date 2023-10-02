package com.crmfoodestablishment.user_auth_service.auth_sevice.service;

import com.crmfoodestablishment.user_auth_service.auth_sevice.service.model.AccessToken;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.model.AccessTokenClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;

public class AccessTokenHandlerAdapter extends JwtHandlerAdapter<AccessToken> {

    @Override
    public AccessToken onClaimsJws(Jws<Claims> jws) {
        return new AccessToken(
                jws.getHeader(),
                new AccessTokenClaims(
                        jws.getBody().getIssuedAt(),
                        jws.getBody().getExpiration()
                )
        );
    }
}
