package com.crm.food.establishment.user.auth.token.adapter;

import com.crm.food.establishment.user.auth.token.RefreshToken;
import com.crm.food.establishment.user.auth.token.RefreshTokenClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;

import java.util.UUID;

import static com.crm.food.establishment.user.auth.service.TimeUtils.convertDateToLocalDateTime;

public class RefreshTokenHandlerAdapter extends JwtHandlerAdapter<RefreshToken> {

    @Override
    public RefreshToken onClaimsJws(Jws<Claims> jws) {
        return new RefreshToken(
                jws.getHeader(),
                new RefreshTokenClaims(
                        convertDateToLocalDateTime(jws.getBody().getIssuedAt()),
                        convertDateToLocalDateTime(jws.getBody().getExpiration()),
                        UUID.fromString(jws.getBody().getSubject())
                )
        );
    }
}
