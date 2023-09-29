package com.crmfoodestablishment.user_auth_service.auth_sevice.service;

import com.crmfoodestablishment.user_auth_service.auth_sevice.exception.InvalidTokenException;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.model.AccessToken;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.model.AccessTokenClaims;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.model.RefreshToken;
import com.crmfoodestablishment.user_auth_service.user_manager.entity.User;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final PrivateKey refreshTokenSecretKey;
    private final Long refreshTokenExpirationTime;
    private final JwtParser refreshTokenParser;

    private final PrivateKey accessTokenSecretKey;
    private final Long accessTokenExpirationTime;
    private final JwtParser accessTokenParser;

    private final StringRedisTemplate refreshTokenRedisTemplate;

    public JwtServiceImpl(
            @Value("${auth.jwt.refreshToken.secretKey}")
            String refreshTokenSecretKey,
            @Value("${auth.jwt.refreshToken.publicKey}")
            String refreshTokenPublicKey,
            @Value("${auth.jwt.refreshToken.expirationTime}")
            Long refreshTokenExpirationTime,
            @Value("${auth.jwt.accessToken.secretKey}")
            String accessTokenSecretKey,
            @Value("${auth.jwt.accessToken.publicKey}")
            String accessTokenPublicKey,
            @Value("${auth.jwt.accessToken.expirationTime}")
            Long accessTokenExpirationTime,

            StringRedisTemplate refreshTokenRedisTemplate
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        this.refreshTokenSecretKey = convertStringToPrivateKey(refreshTokenSecretKey, keyFactory);
        PublicKey refreshTokenPublicKeyObject = convertStringToPublicKey(refreshTokenPublicKey, keyFactory);
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        refreshTokenParser = Jwts.parserBuilder()
                .setSigningKey(refreshTokenPublicKeyObject)
                .build();


        this.accessTokenSecretKey = convertStringToPrivateKey(accessTokenSecretKey, keyFactory);
        PublicKey accessTokenPublicKeyObject = convertStringToPublicKey(accessTokenPublicKey, keyFactory);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        accessTokenParser = Jwts.parserBuilder()
                .setSigningKey(accessTokenPublicKeyObject)
                .build();

        this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
    }

    @Override
    public String issueAccessToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(accessTokenExpirationTime);

        String token;

        token = Jwts.builder()
                .setIssuedAt(Date.from(issuedTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
                .claim("permissions", user.getUserPermissionList())
                .signWith(accessTokenSecretKey, SignatureAlgorithm.RS256)
                .compact();

        log.info("Issued access token for user: " + user.getEmail());
        return token;
    }

    @Override
    public String issueRefreshToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(refreshTokenExpirationTime);

        String token;

        token = Jwts.builder()
                .setIssuedAt(Date.from(issuedTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setSubject(user.getEmail())
                .signWith(refreshTokenSecretKey, SignatureAlgorithm.RS256)
                .compact();

        refreshTokenRedisTemplate.opsForValue()
                .set(
                        user.getEmail(),
                        token,
                        refreshTokenExpirationTime,
                        TimeUnit.SECONDS
                );

        log.info("Issued refresh token for user: " + user.getEmail());
        return token;
    }

    @Override
    public void invalidateRefreshToken(String userEmail) {
        refreshTokenRedisTemplate.delete(userEmail);
        log.info("Invalidated refresh token for user: " + userEmail);
    }

    @Override
    public AccessToken parseAccessToken(String accessToken) {
        AccessToken parsedAccessToken;

        try {
            parsedAccessToken = accessTokenParser.parse(accessToken, new JwtHandlerAdapter<>() {
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
            });
        } catch (SignatureException e) {
            throw new InvalidTokenException("Invalid token: given token with invalid signature");
        }


        if (parsedAccessToken.claims().expiration().before(new Date())) {
            throw new InvalidTokenException(
                    "Given invalid JWT: it has expired"
            );
        }
        return parsedAccessToken;
    }

    @Override
    public RefreshToken parseRefreshToken(String refreshToken) {
        Jws<Claims> parsedRefreshToken;

        try {
            parsedRefreshToken = refreshTokenParser.parseClaimsJws(refreshToken);
        } catch (SignatureException e) {
            throw new InvalidTokenException("Invalid token: given token with invalid signature");
        }

        if (Boolean.FALSE.equals(refreshTokenRedisTemplate.hasKey(parsedRefreshToken
                .getBody()
                .getSubject()))
        ) {
            throw new InvalidTokenException(
                    "Given invalid refresh token: no such tokens for that subject"
            );
        }

        return new RefreshToken(
                parsedRefreshToken.getHeader(),
                parsedRefreshToken.getBody()
        );
    }

    private PrivateKey convertStringToPrivateKey(
            String token,
            KeyFactory keyFactory
    ) throws InvalidKeySpecException {
        byte[] decodedRefreshTokenSecretKey = Base64.getDecoder().decode(token);
        var tokenSecretKeySpec = new PKCS8EncodedKeySpec(decodedRefreshTokenSecretKey);
        return keyFactory.generatePrivate(tokenSecretKeySpec);
    }

    private PublicKey convertStringToPublicKey(
            String token,
            KeyFactory keyFactory
    ) throws InvalidKeySpecException {
        byte[] decodedRefreshTokenPublicKey = Base64.getDecoder().decode(token);
        var tokenPublicKeySpec = new X509EncodedKeySpec(decodedRefreshTokenPublicKey);
        return keyFactory.generatePublic(tokenPublicKeySpec);
    }
}
