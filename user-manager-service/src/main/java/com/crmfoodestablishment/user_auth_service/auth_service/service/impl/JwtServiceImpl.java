package com.crmfoodestablishment.user_auth_service.auth_service.service.impl;

import com.crmfoodestablishment.user_auth_service.auth_service.exception.InvalidTokenException;
import com.crmfoodestablishment.user_auth_service.auth_service.service.token.adapter.AccessTokenHandlerAdapter;
import com.crmfoodestablishment.user_auth_service.auth_service.service.JwtService;
import com.crmfoodestablishment.user_auth_service.auth_service.service.token.adapter.RefreshTokenHandlerAdapter;
import com.crmfoodestablishment.user_auth_service.auth_service.service.token.AccessToken;
import com.crmfoodestablishment.user_auth_service.auth_service.service.token.RefreshToken;
import com.crmfoodestablishment.user_auth_service.user_manager.entity.User;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.crmfoodestablishment.user_auth_service.auth_service.service.TimeUtils.convertLocalDateTimeToDate;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final PrivateKey refreshTokenSecretKey;
    private final Long refreshTokenExpirationTime;
    private final JwtParser refreshTokenParser;
    private final RefreshTokenHandlerAdapter refreshTokenHandlerAdapter;

    private final PrivateKey accessTokenSecretKey;
    private final Long accessTokenExpirationTime;
    private final JwtParser accessTokenParser;
    private final AccessTokenHandlerAdapter accessTokenHandlerAdapter;

    private final RedisTemplate<UUID, String> refreshTokenRedisTemplate;

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

            RedisTemplate<UUID, String> refreshTokenRedisTemplate
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        this.refreshTokenSecretKey = convertStringTokenToPrivateKey(refreshTokenSecretKey, keyFactory);
        PublicKey refreshTokenPublicKeyObject = convertStringTokenToPublicKey(refreshTokenPublicKey, keyFactory);
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        refreshTokenParser = Jwts.parserBuilder()
                .setSigningKey(refreshTokenPublicKeyObject)
                .build();
        refreshTokenHandlerAdapter = new RefreshTokenHandlerAdapter();

        this.accessTokenSecretKey = convertStringTokenToPrivateKey(accessTokenSecretKey, keyFactory);
        PublicKey accessTokenPublicKeyObject = convertStringTokenToPublicKey(accessTokenPublicKey, keyFactory);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        accessTokenParser = Jwts.parserBuilder()
                .setSigningKey(accessTokenPublicKeyObject)
                .build();
        accessTokenHandlerAdapter = new AccessTokenHandlerAdapter();

        this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
    }

    @Override
    public String issueAccessToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(accessTokenExpirationTime);

        String token = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .claim("permissions", user.getPermissionsList())
                .signWith(accessTokenSecretKey, SignatureAlgorithm.RS256)
                .compact();

        log.info("Issued access token for user: " + user.getEmail());
        return token;
    }

    @Override
    public String issueRefreshToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(refreshTokenExpirationTime);

        String token = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .signWith(refreshTokenSecretKey, SignatureAlgorithm.RS256)
                .compact();

        refreshTokenRedisTemplate.opsForValue()
                .set(
                        user.getUuid(),
                        token,
                        refreshTokenExpirationTime,
                        TimeUnit.SECONDS
                );

        log.info("Issued refresh token for user: " + user.getEmail());
        return token;
    }

    @Override
    public void invalidateRefreshToken(UUID userUuid) {
        refreshTokenRedisTemplate.delete(userUuid);
        log.info("Invalidated refresh token for user: " + userUuid);
    }

    @Override
    public AccessToken parseAccessToken(String accessToken) {
        AccessToken parsedAccessToken;

        try {
            parsedAccessToken = accessTokenParser.parse(
                    accessToken,
                    accessTokenHandlerAdapter
            );
        } catch (RuntimeException e) {
            throw new InvalidTokenException(
                    "Invalid token: given invalid access token"
            );
        }

        if (parsedAccessToken.claims().exp().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException(
                    "Invalid token: given token has expired"
            );
        }

        return parsedAccessToken;
    }

    @Override
    public RefreshToken parseRefreshToken(String refreshToken) {
        RefreshToken parsedRefreshToken;

        try {
            parsedRefreshToken = refreshTokenParser.parse(
                    refreshToken,
                    refreshTokenHandlerAdapter
            );
        } catch (RuntimeException e) {
            throw new InvalidTokenException(
                    "Invalid token: given invalid refresh token"
            );
        }

        if (Boolean.FALSE.equals(refreshTokenRedisTemplate.hasKey(
                parsedRefreshToken.claims().sub()
        ))) {
            throw new InvalidTokenException(
                    "Invalid token: given refresh token has expired"
            );
        }

        return parsedRefreshToken;
    }

    private PrivateKey convertStringTokenToPrivateKey(
            String token,
            KeyFactory keyFactory
    ) throws InvalidKeySpecException {
        byte[] decodedRefreshTokenSecretKey = Base64.getDecoder().decode(token);
        var tokenSecretKeySpec = new PKCS8EncodedKeySpec(decodedRefreshTokenSecretKey);
        return keyFactory.generatePrivate(tokenSecretKeySpec);
    }

    private PublicKey convertStringTokenToPublicKey(
            String token,
            KeyFactory keyFactory
    ) throws InvalidKeySpecException {
        byte[] decodedRefreshTokenPublicKey = Base64.getDecoder().decode(token);
        var tokenPublicKeySpec = new X509EncodedKeySpec(decodedRefreshTokenPublicKey);
        return keyFactory.generatePublic(tokenPublicKeySpec);
    }
}
