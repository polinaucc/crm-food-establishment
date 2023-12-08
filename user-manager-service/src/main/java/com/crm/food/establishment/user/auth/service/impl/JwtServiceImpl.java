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

import static com.crm.food.establishment.user.auth.service.KeyUtils.convertStringTokenToPrivateKey;
import static com.crm.food.establishment.user.auth.service.KeyUtils.convertStringTokenToPublicKey;
import static com.crm.food.establishment.user.auth.service.TimeUtils.convertLocalDateTimeToDate;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    private final PrivateKey refreshTokenSecretKey;
    private final JwtParser refreshTokenParser;
    private final RefreshTokenHandlerAdapter refreshTokenHandlerAdapter;

    private final PrivateKey accessTokenSecretKey;
    private final JwtParser accessTokenParser;
    private final AccessTokenHandlerAdapter accessTokenHandlerAdapter;

    private final RedisTemplate<UUID, String> refreshTokenRedisTemplate;

    public JwtServiceImpl(
            JwtProperties jwtProperties,
            RedisTemplate<UUID, String> refreshTokenRedisTemplate
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        this.jwtProperties = jwtProperties;

        refreshTokenSecretKey = convertStringTokenToPrivateKey(
                jwtProperties.refreshToken().secretKey(),
                keyFactory
        );
        PublicKey refreshTokenPublicKey = convertStringTokenToPublicKey(
                jwtProperties.refreshToken().publicKey(),
                keyFactory
        );
        refreshTokenParser = Jwts.parserBuilder()
                .setSigningKey(refreshTokenPublicKey)
                .build();
        refreshTokenHandlerAdapter = new RefreshTokenHandlerAdapter();

        accessTokenSecretKey = convertStringTokenToPrivateKey(
                jwtProperties.accessToken().secretKey(),
                keyFactory
        );
        PublicKey accessTokenPublicKey = convertStringTokenToPublicKey(
                jwtProperties.accessToken().publicKey(),
                keyFactory
        );
        accessTokenParser = Jwts.parserBuilder()
                .setSigningKey(accessTokenPublicKey)
                .build();
        accessTokenHandlerAdapter = new AccessTokenHandlerAdapter();

        this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
    }

    @Override
    public String issueAccessToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(
                jwtProperties.accessToken().expirationTimeInMinutes()
        );

        String token = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .claim("role", user.getRole())
                .signWith(accessTokenSecretKey, SignatureAlgorithm.RS256)
                .compact();

        log.info("Issued access token for user: " + user.getEmail());
        return token;
    }

    @Override
    public String issueRefreshToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(
                jwtProperties.refreshToken().expirationTimeInMinutes()
        );

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
                        jwtProperties.refreshToken().expirationTimeInMinutes(),
                        TimeUnit.MINUTES
                );

        log.info("Issued refresh token for user: " + user.getEmail());
        return token;
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
        } catch (RuntimeException e) {
            throw new InvalidTokenException(
                    "Given invalid access token"
            );
        }

        if (parsedAccessToken.claims().exp().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException(
                    "Given access token has expired"
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
                    "Given invalid refresh token"
            );
        }

        if (Boolean.FALSE.equals(refreshTokenRedisTemplate.hasKey(
                parsedRefreshToken.claims().sub()
        ))) {
            throw new InvalidTokenException(
                    "Given refresh token has expired"
            );
        }

        return parsedRefreshToken;
    }
}
