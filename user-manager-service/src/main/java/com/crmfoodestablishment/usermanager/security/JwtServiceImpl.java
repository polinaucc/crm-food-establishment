package com.crmfoodestablishment.usermanager.security;

import com.crmfoodestablishment.usermanager.entity.User;
import com.crmfoodestablishment.usermanager.exceptions.InvalidTokenException;
import com.crmfoodestablishment.usermanager.security.models.AccessToken;
import com.crmfoodestablishment.usermanager.security.models.AccessTokenClaims;
import com.crmfoodestablishment.usermanager.security.models.RefreshToken;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
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

@Service
public class JwtServiceImpl implements JwtService {

    private final PrivateKey refreshTokenSecretKey;
    private final PublicKey refreshTokenPublicKey;
    private final Long refreshTokenExpirationTime;
    private final JwtParser refreshTokenParser;

    private final PrivateKey accessTokenSecretKey;
    private final PublicKey accessTokenPublicKey;
    private final Long accessTokenExpirationTime;
    private final JwtParser accessTokenParser;

    private final RefreshTokenRepository refreshTokenRepository;

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

            RefreshTokenRepository refreshTokenRepository
    ) throws NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance("RSA");

        byte[] decodedRefreshTokenSecretKey = Base64.getDecoder().decode(refreshTokenSecretKey);
        PKCS8EncodedKeySpec refreshTokenSecretKeySpec = new PKCS8EncodedKeySpec(decodedRefreshTokenSecretKey);
        try {
            this.refreshTokenSecretKey = kf.generatePrivate(refreshTokenSecretKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Invalid refresh token secret key");
        }
        byte[] decodedRefreshTokenPublicKey = Base64.getDecoder().decode(refreshTokenPublicKey);
        X509EncodedKeySpec refreshTokenPublicKeySpec = new X509EncodedKeySpec(decodedRefreshTokenPublicKey);
        try {
            this.refreshTokenPublicKey = kf.generatePublic(refreshTokenPublicKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Invalid refresh token public key");
        }
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        refreshTokenParser = Jwts.parserBuilder()
                .setSigningKey(this.refreshTokenPublicKey)
                .build();

        byte[] decodedAccessTokenSecretKey = Base64.getDecoder().decode(accessTokenSecretKey);
        PKCS8EncodedKeySpec accessTokenSecretKeySpec = new PKCS8EncodedKeySpec(decodedAccessTokenSecretKey);
        try {
            this.accessTokenSecretKey = kf.generatePrivate(accessTokenSecretKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Invalid access token secret key");
        }
        byte[] decodedAccessTokenPublicKey = Base64.getDecoder().decode(accessTokenPublicKey);
        X509EncodedKeySpec accessTokenPublicKeySpec = new X509EncodedKeySpec(decodedAccessTokenPublicKey);
        try {
            this.accessTokenPublicKey = kf.generatePublic(accessTokenPublicKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Invalid access token public key");
        }
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        accessTokenParser = Jwts.parserBuilder()
                .setSigningKey(this.accessTokenPublicKey)
                .build();

        this.refreshTokenRepository = refreshTokenRepository;
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

        //TODO write logic of saving refresh token to whitelistBD with expiration
        refreshTokenRepository.save(user.getEmail(), token);

        return token;
    }

    @Override
    public void invalidateRefreshToken(String userEmail) {
        refreshTokenRepository.deleteById(userEmail);
    }

    @Override
    public AccessToken parseAccessToken(String accessToken) {
        return accessTokenParser.parse(accessToken, new JwtHandlerAdapter<>() {
            @Override
            public AccessToken onClaimsJws(Jws<Claims> jws) {
                if (jws.getBody().getExpiration().before(new Date())) {
                    throw new InvalidTokenException(
                            "Given invalid JWT: it has expired"
                    );
                }

                return new AccessToken(
                        jws.getHeader(),
                        new AccessTokenClaims(
                                jws.getBody().getIssuedAt(),
                                jws.getBody().getExpiration()
                        )
                );
            }
        });
    }

    @Override
    public RefreshToken parseRefreshToken(String refreshToken) {
        Jws<Claims> parsedRefreshToken = refreshTokenParser.parseClaimsJws(refreshToken);

        if (!refreshTokenRepository.exists(parsedRefreshToken.getBody().getSubject())) {
            throw new InvalidTokenException(
                    "Given invalid refresh token: no such tokens for that subject"
            );
        }

        return new RefreshToken(
                parsedRefreshToken.getHeader(),
                parsedRefreshToken.getBody()
        );
    }
}
