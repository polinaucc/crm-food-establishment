package com.crm.food.establishment.user.auth.service.impl;

import com.crm.food.establishment.user.auth.service.KeyUtils;
import com.crm.food.establishment.user.auth.config.jwt.AccessTokenProperties;
import com.crm.food.establishment.user.auth.config.jwt.JwtProperties;
import com.crm.food.establishment.user.auth.config.jwt.RefreshTokenProperties;
import com.crm.food.establishment.user.auth.exception.InvalidTokenException;
import com.crm.food.establishment.user.auth.token.AccessToken;
import com.crm.food.establishment.user.auth.token.RefreshToken;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.auth.token.adapter.AccessTokenHandlerAdapter;
import com.crm.food.establishment.user.auth.token.adapter.RefreshTokenHandlerAdapter;
import com.crm.food.establishment.user.manager.entity.Role;
import com.crm.food.establishment.user.manager.entity.User;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.crm.food.establishment.user.auth.service.TimeUtils.convertLocalDateTimeToDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private RedisTemplate<UUID, String> refreshTokenRedisTemplate;

    @Mock
    private ValueOperations<UUID, String> valueOperations;

    private PrivateKey accessTokenSecretKey;
    private JwtParser accessTokenParser;
    private AccessTokenHandlerAdapter accessTokenHandlerAdapter;
    private Long accessTokenExpirationTime;

    private PrivateKey refreshTokenSecretKey;
    private JwtParser refreshTokenParser;
    private RefreshTokenHandlerAdapter refreshTokenHandlerAdapter;
    private Long refreshTokenExpirationTime;

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException {
        JwtProperties jwtProperties = new JwtProperties(
                new AccessTokenProperties(
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAorqA+wc6p4Em2j/Zte/o67q2eGgmB4MYyqCJWJTcaCCRZ2CBisLknmDolna51CKVzKjLM65WC7IyD8qqcHwq6QCGDDIQd9/9DfzkPruuS9CTeyez4xyGpEsStFWKPGcm9EaWxlhDMs1VZJ6Lp48yV9ETCY8rPz3PVkPL/oa3lrO7T1ts4mQa2Sq4Yy6TmtkPw9QxCvD+qYm4GfJjnAtPrRNkYEAfibYwqltnm3HA1QHcoQXrh2zvVxxZxQbTvHIW6CCD2TCyt0gXezr0A/NThlq3zI0vMO5EHO2mN5EKZ3fVYjmLeZj0RzZvb0MBu/2NWBgui3/dckA6v4Y4/8z4JwIDAQAB",
                        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCiuoD7BzqngSbaP9m17+jrurZ4aCYHgxjKoIlYlNxoIJFnYIGKwuSeYOiWdrnUIpXMqMszrlYLsjIPyqpwfCrpAIYMMhB33/0N/OQ+u65L0JN7J7PjHIakSxK0VYo8Zyb0RpbGWEMyzVVknounjzJX0RMJjys/Pc9WQ8v+hreWs7tPW2ziZBrZKrhjLpOa2Q/D1DEK8P6pibgZ8mOcC0+tE2RgQB+JtjCqW2ebccDVAdyhBeuHbO9XHFnFBtO8chboIIPZMLK3SBd7OvQD81OGWrfMjS8w7kQc7aY3kQpnd9ViOYt5mPRHNm9vQwG7/Y1YGC6Lf91yQDq/hjj/zPgnAgMBAAECggEACjSEDjof1Y1l8F38u0o2Y0IsW1S8vc/c6Cg1NNbxE56nP8JvnVInoD8XlwgPE1yv5EvZon4Wa1lbhl4BXliLIF2mCnHBUqM7bXsknUKL5blI5npQ77dEQl1q6OBMcQlPCha7MekrHa5+4naG75UtZRB5jynpXmfz7scFrgqozMyTED1efGfNiA9RDrdPKcmwQD50HUL6tv6JWox9gfhO76EwV9WirY9L8Ptybi03FZ7Z1/c+joSWwVKqgyAkUmreUFaJ+1tUztHFhsUBOv9rNAStkmdJhCId3iH6njJ1iYZtMpzqwFtT1Z7a+lAcSddFDl5LQT1bphJcH11/b6A2MQKBgQDcIW2v7lzGefBweGmyOaQyhmtjpmAxlyG0z0NiPMeBEP6pm3yIUPUX5V1l1wwOpU5egsQVcoU1cyyxmVKrKwDfoGCNd8ENgXs6tyZxEJU78rYlvhCb5MNSeSi+XVQuxNf3ppcd0T8m/GyVD5EXHBPhveM63aOaTqGyecv9+emgmwKBgQC9Ppg8KFTxd/cQ2m1P7us9CqnurADLdD5iJlFScPdDFycLLgfTUQw+/xT3E8Xp0abRx/vq5n4CBjro9lhhtn3AnEhZ7AFjDUcMUc/+5t6j4eIC2nxUzCDadBEZLEPZxILLmt1EWB696RRPpmKmXGvLZC6fPzNIx866/b9JxwABZQKBgAj8KFD9BrZnNcQxdgb7SVNynyGDIfwEMjECILr6xh3jhrF/kjayLz7ZctV8UJascqVy6vQBJ6TF3bYmDvTDnZaDIk3D7JJTjacY4K3Ownie8IZb2quyS7KrFVmnz47VJAeMujBSYSSDOx9eckqiL8GQEZ1OfKYIvZreU9A7CikvAoGBAK9x1oT/BzEN/Pmc5vbSEzh2KHDi+aGlVSh9Satam4GRd1sAA7U/UFEe/vrJVBSfjz6xXrW5llyeFgNgTzPXkeH9gX4MNIT2DEY5mtFAjGv2wCfREq8yx8p/DFMAHK9JZdEI89WmtGlwKqplINQGntDjQe3VE+CHjPsCwFLPsQuZAoGAKZMMLv/F9Rih2DtQoQCpm2buYadi+aaMAc4byzVJL8L89oHHdr41IkuImrtT8iJjCH1mLiJuqbqDnVBS4wluCHra/O3og7XpkUoeGy6VpsPLSsOoIkUT1qU+v3B+QOovOytutNv3CeQ+arZevMwVMbRmRqFn/aiJVf52IEvHpj0=",
                        100000000L
                ),
                new RefreshTokenProperties(
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtF2wlWsXpK/zxWeIsfJqEKl6I9wYZbBcZmmT9hkyO4y+6xnopFmQQOpY0dCXjx77ChaeKSYQhrb1uZbCl+tE0G7XgueoppEUASIOjwjI1X+2a6Ai1zvWC2OoJQqorW/J64xhSh6Dv/IOMOCBM2drbQDqbnBmHkDleAjZTSwVqJpVKfRMGre6lcVCvVWl+tVKhgeVGNx8qood3tiH05jpphT/X8EA7kh+oQhcG92f+VTqsnol/S1xN828CW9lQ1nWe2ZW7zZ09mxoqheIkQ6IWIgJ7Si6qY/CzHt667RlMxxy/2X9Wf+IU7tDWaKRHnloXRcqFJud+3R+r81z4PceSwIDAQAB",
                        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0XbCVaxekr/PFZ4ix8moQqXoj3BhlsFxmaZP2GTI7jL7rGeikWZBA6ljR0JePHvsKFp4pJhCGtvW5lsKX60TQbteC56imkRQBIg6PCMjVf7ZroCLXO9YLY6glCqitb8nrjGFKHoO/8g4w4IEzZ2ttAOpucGYeQOV4CNlNLBWomlUp9Ewat7qVxUK9VaX61UqGB5UY3Hyqih3e2IfTmOmmFP9fwQDuSH6hCFwb3Z/5VOqyeiX9LXE3zbwJb2VDWdZ7ZlbvNnT2bGiqF4iRDohYiAntKLqpj8LMe3rrtGUzHHL/Zf1Z/4hTu0NZopEeeWhdFyoUm537dH6vzXPg9x5LAgMBAAECggEASaU9rmzpXQHJJ+/j5UDFpSMGL7E1NwRBXkG6vBBCrgUEiYttLfoyCAeIF7ezwqkUOOTOVJ/pvI/lGo/fydczJGY10goUF1sCj4nf5vBfzKkWi7B5uWeBuOnKpMFK6XJNQMPzj6eRBSVpPPTWXG5cc0gd8NUtJSo7Z/04+SVIfKxDIkEDzCRjB2qrUE9UY6LApDJ7bAaCEfG2bNp+OkN8fuD0cK746eG0EXuUP5znYnzRGxb+KAZMMGRfj0+GuJPhsdwPh/5Zv4QDrEeGUDbuxV1u4TePltaqSqmxrKHtlqkFnf70bj8e2xm8Az6ATioDhWkY2DeajZZztZgJKndglQKBgQDBIkBlqTvkR55DmAKFvAvBq8BdOrJ6JLnsTXQqwpYi1EGmIkOHE7RnMk2VDnYilFtFa/6vy1EmvH+TRVwUreTiUKiklNF1sbHBHPNIJNx/iX51rKr4ZD/1kLHv2y5KJXnGTFm10Lmsu9qhe8UQ7TGzi0XTuctIOskI+MjX697VlQKBgQDvE4B4GEDfBIP8zpPLf7+gKFiV3r+THTyr8I/0EwSO7+OvelST0vKrGi65Q9FzWH0cOUZF9s9+Ul+kkRf84Qcp7Ljyp7a0OBHi+NirSP4QP7Am7o2Vg5CHWbi7NyOK7ORyoR/DQW5o98EITCeeRf0roQ4d7C4ktj8uXbXmyb9sXwKBgQCWJxL59sDhkeyxkJUldDWPtQAxe1kXLUZ/ORh/xhkDqxH/H9RwSBmRwcIfiMa/y6fKKVQxSFknTJXC4cHdMfUpHpx+mFTgs+4GvYNHK6FiZnsy8bNaulYfjQdJ+5XZf5lVWjbE7Cnu6X3o3lkv97kUZcs7+M7asRlrKEC93lu4oQKBgChGAL46V7M574IGyp7dRkqUgVe9zkfaBP5x584V5g6uGozU7qp4PqjUIErVQdNMdMbsXwoqKYn+Fl6CkqfmwG4vknhOCFkTmO/DO7ye7y2NjP8B8JffMdmbI6NmBnbAsTseKVubCh5knf/n1ES1RZCz8MzZnGxSxxHod1wJYFXBAoGAenWYuwvDp4oEm8hDkR1fzAaRkX2ysVz+a4O/eglILrgrgE14JGBPnYwSLjf6hullStmUG6fOG3I6e5poRgHHRy0Z5FQLzqvrTOvfUK022DE3MUz4kKxKdRP7Yy5+KRr4LHHghGBy4j+GfNoBi89ys4bi8GJVlfaAr75QXsb0vKM=",
                        1000000000L
                )
        );

        jwtService = new JwtServiceImpl(
                jwtProperties,
                refreshTokenRedisTemplate
        );

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        accessTokenSecretKey = KeyUtils.convertStringTokenToPrivateKey(
                jwtProperties.accessToken().secretKey(),
                keyFactory
        );
        accessTokenParser = Jwts.parserBuilder()
                .setSigningKey(accessTokenSecretKey)
                .build();
        accessTokenHandlerAdapter = new AccessTokenHandlerAdapter();
        accessTokenExpirationTime = jwtProperties.accessToken().expirationTime();

        refreshTokenSecretKey = KeyUtils.convertStringTokenToPrivateKey(
                jwtProperties.refreshToken().secretKey(),
                keyFactory
        );
        refreshTokenParser = Jwts.parserBuilder()
                .setSigningKey(KeyUtils.convertStringTokenToPublicKey(
                        jwtProperties.refreshToken().publicKey(),
                        keyFactory
                ))
                .build();
        refreshTokenHandlerAdapter = new RefreshTokenHandlerAdapter();
        refreshTokenExpirationTime = jwtProperties.refreshToken().expirationTime();
    }

    @Test
    void issueAccessToken_ReturnsValidAccessToken() {
        User user = new User(UUID.randomUUID());
        user.setRole(Role.CLIENT);
        user.setEmail("test@gmail.com");

        String accessToken = jwtService.issueAccessToken(user);

        AccessToken parsedAccessToken = accessTokenParser.parse(
                accessToken,
                accessTokenHandlerAdapter
        );
        assertEquals(user.getUuid(), parsedAccessToken.claims().sub());
        assertEquals(user.getRole(), parsedAccessToken.claims().role());
        assertEquals(
                LocalDateTime.now()
                        .withNano(0),
                parsedAccessToken.claims().iat()
        );
        assertEquals(
                LocalDateTime.now()
                        .plusMinutes(accessTokenExpirationTime)
                        .withNano(0),
                parsedAccessToken.claims().exp()
        );
    }

    @Test
    void issueRefreshToken_ReturnsValidRefreshToken_And_SafesToRedis() {
        User user = new User(UUID.randomUUID());
        user.setEmail("test@gmail.com");

        when(refreshTokenRedisTemplate.opsForValue())
                .thenReturn(valueOperations);

        String refreshToken = jwtService.issueRefreshToken(user);

        verify(valueOperations, times(1))
                .set(
                        user.getUuid(),
                        refreshToken,
                        refreshTokenExpirationTime,
                        TimeUnit.MINUTES
                );

        RefreshToken parsedAccessToken = refreshTokenParser.parse(
                refreshToken,
                refreshTokenHandlerAdapter
        );
        assertEquals(user.getUuid(), parsedAccessToken.claims().sub());
        assertEquals(
                LocalDateTime.now()
                        .withNano(0),
                parsedAccessToken.claims().iat()
        );
        assertEquals(
                LocalDateTime.now()
                        .plusMinutes(refreshTokenExpirationTime)
                        .withNano(0),
                parsedAccessToken.claims().exp()
        );
    }

    @Test
    void issueTokenPair_ReturnsTokenPair() {
        User user = new User(UUID.randomUUID());
        user.setRole(Role.CLIENT);
        user.setEmail("test@gmail.com");

        when(refreshTokenRedisTemplate.opsForValue())
                .thenReturn(valueOperations);

        TokenPair refreshToken = jwtService.issueTokenPair(user);

        assertNotNull(refreshToken);
        assertNotNull(refreshToken.getRefreshToken());
        assertNotNull(refreshToken.getAccessToken());
    }

    @Test
    void invalidateRefreshToken_DeletesTokenInRedis() {
        UUID testUuid = UUID.randomUUID();

        jwtService.invalidateRefreshToken(testUuid);

        verify(refreshTokenRedisTemplate, times(1))
                .delete(testUuid);
    }

    @Test
    void parseAccessToken_ValidatesInputString() {
        assertThrows(
                InvalidTokenException.class,
                () -> jwtService.parseAccessToken("testToken")
        );
    }

    @Test
    void parseAccessToken_ValidatesExpirationTime() {
        User user = new User(UUID.randomUUID());
        user.setRole(Role.CLIENT);
        user.setEmail("test@gmail.com");
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.minusMinutes(60);

        String testToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .claim("role", user.getRole())
                .signWith(accessTokenSecretKey, SignatureAlgorithm.RS256)
                .compact();

        assertThrows(
                InvalidTokenException.class,
                () -> jwtService.parseAccessToken(testToken)
        );
    }

    @Test
    void parseAccessToken_CorrectlyParesesAccessToken() {
        User user = new User(UUID.randomUUID());
        user.setRole(Role.CLIENT);
        user.setEmail("test@gmail.com");
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(
                accessTokenExpirationTime
        );

        String testToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .claim("role", user.getRole())
                .signWith(accessTokenSecretKey, SignatureAlgorithm.RS256)
                .compact();

        AccessToken accessToken = jwtService.parseAccessToken(testToken);

        assertEquals(user.getUuid(), accessToken.claims().sub());
        assertEquals(user.getRole(), accessToken.claims().role());
        assertEquals(issuedTime.withNano(0), accessToken.claims().iat());
        assertEquals(expirationTime.withNano(0), accessToken.claims().exp());
    }

    @Test
    void parseRefreshToken_ValidatesInputString() {
        assertThrows(
                InvalidTokenException.class,
                () -> jwtService.parseRefreshToken("testToken")
        );
    }

    @Test
    void parseRefreshToken_ValidatesExpirationTime() {
        User user = new User(UUID.randomUUID());
        user.setEmail("test@gmail.com");
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(
                refreshTokenExpirationTime
        );

        String testToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .signWith(refreshTokenSecretKey, SignatureAlgorithm.RS256)
                .compact();

        when(refreshTokenRedisTemplate.hasKey(user.getUuid()))
                .thenReturn(false);

        assertThrows(
                InvalidTokenException.class,
                () -> jwtService.parseRefreshToken(testToken)
        );
        verify(refreshTokenRedisTemplate, times(1))
                .hasKey(user.getUuid());
    }

    @Test
    void parseRefreshToken_CorrectlyParesesRefreshToken() {
        User user = new User(UUID.randomUUID());
        user.setEmail("test@gmail.com");
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(
                refreshTokenExpirationTime
        );

        String testToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .signWith(refreshTokenSecretKey, SignatureAlgorithm.RS256)
                .compact();

        when(refreshTokenRedisTemplate.hasKey(user.getUuid()))
                .thenReturn(true);

        RefreshToken accessToken = jwtService.parseRefreshToken(testToken);

        assertEquals(user.getUuid(), accessToken.claims().sub());
        assertEquals(issuedTime.withNano(0), accessToken.claims().iat());
        assertEquals(expirationTime.withNano(0), accessToken.claims().exp());
    }
}