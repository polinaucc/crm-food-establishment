package com.crmfoodestablishment.user_auth_service.auth_sevice.service.token.adapter;

import com.crmfoodestablishment.user_auth_service.auth_sevice.service.token.AccessToken;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.token.AccessTokenClaims;
import com.crmfoodestablishment.user_auth_service.user_manager.entity.Permission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;

import java.util.ArrayList;
import java.util.UUID;

import static com.crmfoodestablishment.user_auth_service.auth_sevice.service.TimeUtils.convertDateToLocalDateTime;

public class AccessTokenHandlerAdapter extends JwtHandlerAdapter<AccessToken> {

    @Override
    public AccessToken onClaimsJws(Jws<Claims> jws) {
        return new AccessToken(
                jws.getHeader(),
                new AccessTokenClaims(
                        convertDateToLocalDateTime(jws.getBody().getIssuedAt()),
                        convertDateToLocalDateTime(jws.getBody().getExpiration()),
                        UUID.fromString(
                                jws.getBody().getSubject()
                        ),
                        jws.getBody().get(
                                "permissions",
                                new ArrayList<Permission>().getClass()
                        )
                )
        );
    }
}
