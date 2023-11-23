package com.crmfoodestablishment.usermanager.auth.token.adapter;

import com.crmfoodestablishment.usermanager.auth.token.RefreshToken;
import com.crmfoodestablishment.usermanager.auth.token.RefreshTokenClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;

import java.util.UUID;

import static com.crmfoodestablishment.usermanager.auth.service.TimeUtils.convertDateToLocalDateTime;

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
