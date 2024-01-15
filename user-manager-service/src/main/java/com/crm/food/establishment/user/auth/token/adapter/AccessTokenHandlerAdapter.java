package com.crm.food.establishment.user.auth.token.adapter;

import com.crm.food.establishment.user.auth.token.AccessToken;
import com.crm.food.establishment.user.auth.token.AccessTokenClaims;
import com.crm.food.establishment.user.manager.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;

import java.util.UUID;

import static com.crm.food.establishment.user.auth.service.TimeUtils.convertDateToLocalDateTime;

public class AccessTokenHandlerAdapter extends JwtHandlerAdapter<AccessToken> {

    @Override
    public AccessToken onClaimsJws(Jws<Claims> jws) {
        return new AccessToken(
                jws.getHeader(),
                new AccessTokenClaims(
                        convertDateToLocalDateTime(jws.getBody().getIssuedAt()),
                        convertDateToLocalDateTime(jws.getBody().getExpiration()),
                        UUID.fromString(jws.getBody().getSubject()),
                        Role.valueOf(jws.getBody().get("role", String.class))
                )
        );
    }
}
