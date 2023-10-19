package com.crmfoodestablishment.user_auth_service.auth_sevice.service.token.adapter;

import com.crmfoodestablishment.user_auth_service.auth_sevice.service.token.RefreshToken;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.token.RefreshTokenClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;

import java.util.UUID;

import static com.crmfoodestablishment.user_auth_service.auth_sevice.service.TimeUtils.convertDateToLocalDateTime;

public class RefreshTokenHandlerAdapter extends JwtHandlerAdapter<RefreshToken> {

    @Override
    public RefreshToken onClaimsJws(Jws<Claims> jws) {
        return new RefreshToken(
                jws.getHeader(),
                new RefreshTokenClaims(
                        convertDateToLocalDateTime(jws.getBody().getIssuedAt()),
                        convertDateToLocalDateTime(jws.getBody().getExpiration()),
                        UUID.fromString(
                                jws.getBody().getSubject()
                        )
                )
        );
    }
}
