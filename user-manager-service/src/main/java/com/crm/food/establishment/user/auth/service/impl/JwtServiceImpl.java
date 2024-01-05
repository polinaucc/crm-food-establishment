package com.crm.food.establishment.user.auth.service.impl;

import com.crm.food.establishment.user.auth.config.jwt.JwtProperties;
import com.crm.food.establishment.user.auth.exception.InvalidTokenException;
import com.crm.food.establishment.user.auth.service.JwtService;
import com.crm.food.establishment.user.auth.token.AccessToken;
import com.crm.food.establishment.user.auth.token.RefreshToken;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.auth.token.adapter.AccessTokenHandlerAdapter;
import com.crm.food.establishment.user.auth.token.adapter.RefreshTokenHandlerAdapter;
import com.crm.food.establishment.user.manager.entity.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.crm.food.establishment.user.auth.service.KeyUtils.convertBase64PrivateKeyToJavaPrivateKey;
import static com.crm.food.establishment.user.auth.service.KeyUtils.convertBase64PublicKeyToJavaPublicKey;
import static com.crm.food.establishment.user.auth.service.TimeUtils.convertLocalDateTimeToDate;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    private final PrivateKey refreshTokenPrivateKey;
    private final JwtParser refreshTokenParser;
    private final RefreshTokenHandlerAdapter refreshTokenHandlerAdapter;

    private final PrivateKey accessTokenPrivateKey;
    private final JwtParser accessTokenParser;
    private final AccessTokenHandlerAdapter accessTokenHandlerAdapter;

    private final RedisTemplate<UUID, String> refreshTokenRedisTemplate;

    public JwtServiceImpl(
            JwtProperties jwtProperties,
            RedisTemplate<UUID, String> refreshTokenRedisTemplate
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");

        this.jwtProperties = jwtProperties;

        refreshTokenPrivateKey = convertBase64PrivateKeyToJavaPrivateKey(
                jwtProperties.refreshToken().secretKey(),
                rsaKeyFactory
        );
        PublicKey refreshTokenPublicKey = convertBase64PublicKeyToJavaPublicKey(
                jwtProperties.refreshToken().publicKey(),
                rsaKeyFactory
        );
        refreshTokenParser = Jwts.parserBuilder()
                .setSigningKey(refreshTokenPublicKey)
                .build();
        refreshTokenHandlerAdapter = new RefreshTokenHandlerAdapter();

        accessTokenPrivateKey = convertBase64PrivateKeyToJavaPrivateKey(
                jwtProperties.accessToken().secretKey(),
                rsaKeyFactory
        );
        PublicKey accessTokenPublicKey = convertBase64PublicKeyToJavaPublicKey(
                jwtProperties.accessToken().publicKey(),
                rsaKeyFactory
        );
        accessTokenParser = Jwts.parserBuilder()
                .setSigningKey(accessTokenPublicKey)
                .build();
        accessTokenHandlerAdapter = new AccessTokenHandlerAdapter();

        this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
    }

    @Override
    public String issueAccessToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime expirationTime = issuedTime.plusMinutes(
                jwtProperties.accessToken().expirationTimeInMinutes()
        );

        String builtAccessToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .claim("role", user.getRole())
                .signWith(accessTokenPrivateKey, SignatureAlgorithm.RS256)
                .compact();

        log.info("Issued access token for user: " + user.getEmail());
        return builtAccessToken;
    }

    @Override
    public String issueRefreshToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime expirationTime = issuedTime.plusMinutes(
                jwtProperties.refreshToken().expirationTimeInMinutes()
        );

        String builtRefreshToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .signWith(refreshTokenPrivateKey, SignatureAlgorithm.RS256)
                .compact();

        refreshTokenRedisTemplate.opsForValue()
                .set(
                        user.getUuid(),
                        builtRefreshToken,
                        jwtProperties.refreshToken().expirationTimeInMinutes(),
                        TimeUnit.MINUTES
                );

        log.info("Issued refresh token for user: " + user.getEmail());
        return builtRefreshToken;
    }

    @Override
    public TokenPair issueTokenPair(User user) {
        String accessToken = issueAccessToken(user);
        String refreshToken = issueRefreshToken(user);

        return new TokenPair(accessToken, refreshToken);
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
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage(), e);
            throw new InvalidTokenException("Given access token has expired");
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            throw new InvalidTokenException("Given invalid access token");
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
            log.error(e.getMessage(), e);
            throw new InvalidTokenException("Given invalid refresh token");
        }

        if (Boolean.FALSE.equals(refreshTokenRedisTemplate.hasKey(
                parsedRefreshToken.claims().sub()
        ))) {
            throw new InvalidTokenException("Given refresh token has expired");
        }

        return parsedRefreshToken;
    }
}
