package com.crmfoodestablishment.userauthservice.authservice.token.adapter;

import com.crmfoodestablishment.userauthservice.authservice.token.AccessToken;
import com.crmfoodestablishment.userauthservice.authservice.token.AccessTokenClaims;
import com.crmfoodestablishment.userauthservice.usermanager.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;

import java.util.ArrayList;
import java.util.UUID;

import static com.crmfoodestablishment.userauthservice.authservice.service.TimeUtils.convertDateToLocalDateTime;

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
                                "roles",
                                new ArrayList<Role>().getClass()
                        )
                )
        );
    }
}
