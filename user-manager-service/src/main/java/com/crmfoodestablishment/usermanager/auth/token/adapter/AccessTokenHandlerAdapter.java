package com.crmfoodestablishment.usermanager.auth.token.adapter;

import com.crmfoodestablishment.usermanager.auth.token.AccessToken;
import com.crmfoodestablishment.usermanager.auth.token.AccessTokenClaims;
import com.crmfoodestablishment.usermanager.crud.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;

import java.util.UUID;

import static com.crmfoodestablishment.usermanager.auth.service.TimeUtils.convertDateToLocalDateTime;

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
                        Role.valueOf(jws.getBody().get(
                                "role",
                                String.class
                        ))
                )
        );
    }
}
