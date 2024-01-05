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

    private PrivateKey accessTokenPrivateKey;
    private JwtParser accessTokenParser;
    private AccessTokenHandlerAdapter accessTokenHandlerAdapter;
    private Long accessTokenExpirationTime;

    private PrivateKey refreshTokenPrivateKey;
    private JwtParser refreshTokenParser;
    private RefreshTokenHandlerAdapter refreshTokenHandlerAdapter;
    private Long refreshTokenExpirationTime;

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException {
        JwtProperties jwtProperties = new JwtProperties(
                new AccessTokenProperties(
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjEsl997Iv0aB2QfIT71j2uxXF/bZ7Eq/zYbfIomLAPuy2qXayIVZBWJsGFWRBIgnEMXSNg97pcclLwPUFwIOQhW1IR+GucdgDLAkQtFtPaQPgOVGPKICBDxhXC8UaovwAIK7PaO8qciXiIXCffvVob/ZhonpXikKIhL0BYhAI8nRB0M3Q5GJqTuYHNmJG6e0Zvok8VdzHR+uD8idSxeCCOe9gR/J2BzSKs4TQI5Z1n4oOwLVhfbtgsKNrsasozRAHMbYGDq7VV/Ip+bJ8ktYk8c1/ndVj0PsISB9Mmc3zIPYY28j0KwD/6r6BUbAneOwZHw/w8feQ/SGNP2l91kr1QIDAQAB",
                        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCMSyX33si/RoHZB8hPvWPa7FcX9tnsSr/Nht8iiYsA+7LapdrIhVkFYmwYVZEEiCcQxdI2D3ulxyUvA9QXAg5CFbUhH4a5x2AMsCRC0W09pA+A5UY8ogIEPGFcLxRqi/AAgrs9o7ypyJeIhcJ9+9Whv9mGieleKQoiEvQFiEAjydEHQzdDkYmpO5gc2Ykbp7Rm+iTxV3MdH64PyJ1LF4II572BH8nYHNIqzhNAjlnWfig7AtWF9u2Cwo2uxqyjNEAcxtgYOrtVX8in5snyS1iTxzX+d1WPQ+whIH0yZzfMg9hjbyPQrAP/qvoFRsCd47BkfD/Dx95D9IY0/aX3WSvVAgMBAAECggEAAl4xrbLcHDpiQ68EHFsb0zH3sFxcG5xmtgnc50t/W0y5nUN9DghjfcSbrXd4Q1Am/1Cky9Z2uvrBr772oU10FH0i+a77X6mz/ygpRzAbZ2scUIvOI9ovgtx1X5BzMu8vZbAkh+r6arqLhY7jFFb1U/hrLABU6OAeydTHEKGBmk5YgAFvmlYVOXNMuX65lMHTXZ0I76fQhR8Te707PxQq5ewmN6mqphPE+3UZbtPSx0lOpP2/s+GeudnUqK+DZNbZzpaUWwNTL4nVTPKj5+dGV/qsX1tUhnxpPmI9TyYUT5BoVFh0eFZOLeoGfoNsj3xFDuh0C8IRZs3kjvMD7Uo5IQKBgQC2N2xBy8n1W33dJdfBDQdUcXdu93kFvqCADheyYPo9/0XCfV1IS4MgsZOdBMnuOdWKcXheRC3BDxqNy8hpOCKyMLxOLlbA3+lq3sNdxXjIkJcNStb1qwatLt0ZkFYf2EraR82is5EVcKpQZ1C4H3lTNBBxsvamZ6wizm8f3GH68QKBgQDFGf08QtD6DEeLffsXSZXkvJoS479zv48zhspqtSzkg25W29OPTSs/tjrVmXnCfQx0rZADjO0FOyybSb8uKSuOcAViFhcnz+iTX7J65juws3TjsKXq6GGQ18S2vgS4r/o4/KFuan5J4k779WIh5RgKwkvg1vjVOUm7Q8lfz2hXJQKBgQCJwMKFx1OJur4fVsGSP5BABXu+FzaV4jQiOwUtJGVqQzbwvqerxPJ/399xgDJMd8p43CmcUjyozAtOETYUXYzVW0SwchXBwEUNNxVJKcWTEsQvF1oE5WOpv1kFP3nsny6PcGMEUm4nYEJRgJmkaJ7nCOA5pIsU6CFZOukFlC06cQKBgEGbuA6yEwvYwIvvQIBs7yc3nHXDN7xYvFsjeAdzDWF3WmgxPkfKISZRC16EeuJBa77+8xwrcc42908tiiTccXI1WaVfww9uhnVf+nIsZxwXcEZGS8RY2/kxyXom4D113m3PshCmBCml2jLGaIbt0kXqjQVnqWaYbjGWi9aHnKy9AoGAfV9PV2ticYxs15a1OJnIyiOEakh3R/zuq4G4f4EH6H9BStrjqDGUNRq428gBsZtpiEcFu8l0g4bes7yln7ld4QabllVIug28mtUraK3s2mCLHAtzx2L4jGSsPBAO5sSBp3WFQ1rzCs9LsONj8KNy9jfGA7BOKzHWzjsY4cIcvLA=",
                        100000000L
                ),
                new RefreshTokenProperties(
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoY5kDqcJCeNfcKViZDrbKT/5PxsTfhrjhCVC7PBhxzluQme6spTzj5zAvFXaTHl9rQHRW1Dae38lf9gg3EI5IJT7fb7qGHE3IUA4CKiNwwJiYpTj5ObEiTtH4Jvr4wI8zi9gebx5/7CtKTM6/YH7fvgPMFJdAyZL3vWp4Uozz3poT6Rmgbw53leE/BrnuqMtKUgXUuRHGBYlN/ilh9ftJQiRBnilBf/fLKVxvPfpb3DQP7k/tvzRuSUY2NXpwxijX9Rtnxu/tICTfh8wXzJOoXceXt1Wvwcy0/lxYavFcJxjPAUADQLm3S40nyFAfVfPEjrWkl3op09qG8Cu5U3TDwIDAQAB",
                        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQChjmQOpwkJ419wpWJkOtspP/k/GxN+GuOEJULs8GHHOW5CZ7qylPOPnMC8VdpMeX2tAdFbUNp7fyV/2CDcQjkglPt9vuoYcTchQDgIqI3DAmJilOPk5sSJO0fgm+vjAjzOL2B5vHn/sK0pMzr9gft++A8wUl0DJkve9anhSjPPemhPpGaBvDneV4T8Gue6oy0pSBdS5EcYFiU3+KWH1+0lCJEGeKUF/98spXG89+lvcNA/uT+2/NG5JRjY1enDGKNf1G2fG7+0gJN+HzBfMk6hdx5e3Va/BzLT+XFhq8VwnGM8BQANAubdLjSfIUB9V88SOtaSXeinT2obwK7lTdMPAgMBAAECggEAEkyIZ80ircnhHbzAiXLN2n0XtGlS4BCX9E1WKHP310gu2tBYqhv1/y51pD94iPtP3AtIf+EyG2qPlaH7jYc7ZPCLEIynLlqQhdskyFJIha3ySpFBGO8h6XCwrQUW3FvSBawBxPgvZ2Hx0yithgO1jVr35B9yz/BZXVPnyJAOO0dHfLMQO0ptIGc7Fw3wrwFDsIoQJfUPRDoMe5edkQJrOHqmvx7PYA8vEQefGe4IiIVvGYRhJkHyrpvY80nGGtgJgNejT7b1V6slZUqPGVs/Su7IgBNkvvyAJwpcj+s7AaFyXJCJ+Ct2hwg61B+oNI21snG8VJ8yR/OLRrG2TcXIVQKBgQDX4g6ZMfOW/IGnpbxCkbrjjz/s6bmO7JaAixLdlGPpztvExCT2wglJyM4FIkRNLoFbz+YoX+x8AqnY+IYGVsIa4vYVFJk1PDKwYuKZG7UmlIRpyl3bDdmXEjhDXlzGfEH3HoqnHxE5w+r46s0FZNKhEreUQdFx3zXae1JlYx7rEwKBgQC/k+jZIlSmKg4qDH4YMRx574tVuDV0S808mHplTa0sJMEbCNAOkXJnSRcz5pimGnvhVfXXmqSLDlFf/XZ4mkCHu7Q89mu4Y+fijQ+11sTz1OSnhqe/KGg8lXY4Ma1gfMuhdbcj8HbCTuFmQp2Ydgid6Ft2pywj837tOI63VOwblQKBgBCJREz/woTiOpw42b7Pot0jb+YZ0RjdjnmyLXPDlhOmwLbFv38XRdbdBsrl4Q14bng16I9XKaomGhSAlOOPabbdg7QBq+qj/c39BRZtROb7oVjpI19QJEaqIp0mcTzS78qaEEzU2GDwXONMOQAcxPWJlX5JvF1Yl6twntMdUJT5AoGAIwXaQp/JE9uKXVuNTcTkrNIDCfe0Jf+ZlKNXdN+fcfuCJfwVdSRX4tP03vMgIvWigg5h9Dl8LUO/0qmWl/OzKl1BVQfDp6D0CnG5Vofd16mq54lxbzvm2nSEeT9zr7kAzRKlGXfjbiBXiodRIkcpyhxpZCappiTkpl95j8CBrYUCgYEAwdLy4LcFrMtsvnT8uSTL9up69kb+nipP3NMv6Q3Z6eLre4GXS3TtHHWk70y/NMyy+oSNkdXPtsOiicKWZVQcu+I61oH9NkMwdhMO6LxY4mJnC6fvUg9gMk1jKy4fiN/sjiJCYdXZT2+hTZgYTt5Y1eNT7+At/7H1vW15ADQB08Q=",
                        1000000000L
                )
        );

        jwtService = new JwtServiceImpl(jwtProperties, refreshTokenRedisTemplate);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        accessTokenPrivateKey = KeyUtils.convertBase64PrivateKeyToJavaPrivateKey(
                jwtProperties.accessToken().secretKey(),
                keyFactory
        );
        accessTokenParser = Jwts.parserBuilder()
                .setSigningKey(KeyUtils.convertBase64PublicKeyToJavaPublicKey(
                        jwtProperties.accessToken().publicKey(),
                        keyFactory
                ))
                .build();
        accessTokenHandlerAdapter = new AccessTokenHandlerAdapter();
        accessTokenExpirationTime = jwtProperties.accessToken().expirationTimeInMinutes();

        refreshTokenPrivateKey = KeyUtils.convertBase64PrivateKeyToJavaPrivateKey(
                jwtProperties.refreshToken().secretKey(),
                keyFactory
        );
        refreshTokenParser = Jwts.parserBuilder()
                .setSigningKey(KeyUtils.convertBase64PublicKeyToJavaPublicKey(
                        jwtProperties.refreshToken().publicKey(),
                        keyFactory
                ))
                .build();
        refreshTokenHandlerAdapter = new RefreshTokenHandlerAdapter();
        refreshTokenExpirationTime = jwtProperties.refreshToken().expirationTimeInMinutes();
    }

    @Test
    void issueAccessToken_ShouldReturnValidAccessToken() {
        User inputUser = new User(UUID.randomUUID());
        inputUser.setRole(Role.CLIENT);
        inputUser.setEmail("test@gmail.com");
        LocalDateTime expectedIat = LocalDateTime.now().withSecond(0).withNano(0);

        String accessToken = jwtService.issueAccessToken(inputUser);

        AccessToken parsedAccessToken = accessTokenParser.parse(
                accessToken,
                accessTokenHandlerAdapter
        );

        assertEquals(expectedIat, parsedAccessToken.claims().iat());
        assertEquals(
                expectedIat.plusMinutes(accessTokenExpirationTime),
                parsedAccessToken.claims().exp()
        );
        assertEquals(inputUser.getUuid(), parsedAccessToken.claims().sub());
        assertEquals(inputUser.getRole(), parsedAccessToken.claims().role());
    }

    @Test
    void issueRefreshToken_ShouldReturnValidRefreshToken_And_SafeItToRedis() {
        User inputUser = new User(UUID.randomUUID());
        inputUser.setEmail("test@gmail.com");

        when(refreshTokenRedisTemplate.opsForValue()).thenReturn(valueOperations);

        String refreshToken = jwtService.issueRefreshToken(inputUser);

        verify(valueOperations, times(1))
                .set(
                        inputUser.getUuid(),
                        refreshToken,
                        refreshTokenExpirationTime,
                        TimeUnit.MINUTES
                );

        RefreshToken parsedAccessToken = refreshTokenParser.parse(
                refreshToken,
                refreshTokenHandlerAdapter
        );
        assertEquals(
                LocalDateTime.now().withNano(0),
                parsedAccessToken.claims().iat()
        );
        assertEquals(
                LocalDateTime.now()
                        .plusMinutes(refreshTokenExpirationTime)
                        .withNano(0),
                parsedAccessToken.claims().exp()
        );
        assertEquals(inputUser.getUuid(), parsedAccessToken.claims().sub());
    }

    @Test
    void issueTokenPair_ShouldReturnTokenPair() {
        User inputUser = new User(UUID.randomUUID());
        inputUser.setRole(Role.CLIENT);
        inputUser.setEmail("test@gmail.com");

        when(refreshTokenRedisTemplate.opsForValue()).thenReturn(valueOperations);

        TokenPair refreshToken = jwtService.issueTokenPair(inputUser);

        assertNotNull(refreshToken);
        assertNotNull(refreshToken.getRefreshToken());
        assertNotNull(refreshToken.getAccessToken());
    }

    @Test
    void invalidateRefreshToken_ShouldDeleteRefreshTokenInRedis() {
        UUID inputUuid = UUID.randomUUID();

        jwtService.invalidateRefreshToken(inputUuid);

        verify(refreshTokenRedisTemplate, times(1)).delete(inputUuid);
    }

    @Test
    void parseAccessToken_ShouldValidateInputString() {
        InvalidTokenException thrownException = assertThrows(
                InvalidTokenException.class,
                () -> jwtService.parseAccessToken("invalidToken")
        );
        assertEquals("Given invalid access token", thrownException.getMessage());
    }

    @Test
    void parseAccessToken_ShouldValidateExpirationTime() {
        User user = new User(UUID.randomUUID());
        user.setRole(Role.CLIENT);
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.minusMinutes(60);

        String expiredToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .claim("role", user.getRole())
                .signWith(accessTokenPrivateKey, SignatureAlgorithm.RS256)
                .compact();

        InvalidTokenException thrownException = assertThrows(
                InvalidTokenException.class,
                () -> jwtService.parseAccessToken(expiredToken)
        );
        assertEquals("Given access token has expired", thrownException.getMessage());
    }

    @Test
    void parseAccessToken_ShouldReturnCorrectlyParsedAccessToken() {
        User inputUser = new User(UUID.randomUUID());
        inputUser.setRole(Role.CLIENT);
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(accessTokenExpirationTime);

        String inputToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(inputUser.getUuid().toString())
                .claim("role", inputUser.getRole())
                .signWith(accessTokenPrivateKey, SignatureAlgorithm.RS256)
                .compact();

        AccessToken parsedToken = jwtService.parseAccessToken(inputToken);

        assertEquals(issuedTime.withNano(0), parsedToken.claims().iat());
        assertEquals(expirationTime.withNano(0), parsedToken.claims().exp());
        assertEquals(inputUser.getUuid(), parsedToken.claims().sub());
        assertEquals(inputUser.getRole(), parsedToken.claims().role());
    }

    @Test
    void parseRefreshToken_ShouldValidateInputString() {
        InvalidTokenException thrownException = assertThrows(
                InvalidTokenException.class,
                () -> jwtService.parseRefreshToken("invalidToken")
        );
        assertEquals( "Given invalid refresh token", thrownException.getMessage());
    }

    @Test
    void parseRefreshToken_ShouldValidateExpirationTime() {
        User inputUser = new User(UUID.randomUUID());
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(refreshTokenExpirationTime);

        String expiredToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(inputUser.getUuid().toString())
                .signWith(refreshTokenPrivateKey, SignatureAlgorithm.RS256)
                .compact();

        when(refreshTokenRedisTemplate.hasKey(inputUser.getUuid())).thenReturn(false);

        InvalidTokenException thrownException = assertThrows(
                InvalidTokenException.class,
                () -> jwtService.parseRefreshToken(expiredToken)
        );
        assertEquals( "Given refresh token has expired", thrownException.getMessage());
        verify(refreshTokenRedisTemplate, times(1)).hasKey(inputUser.getUuid());
    }

    @Test
    void parseRefreshToken_ShouldReturnCorrectlyParsedRefreshToken() {
        User inputUser = new User(UUID.randomUUID());
        LocalDateTime issuedTime = LocalDateTime.now();
        LocalDateTime expirationTime = issuedTime.plusMinutes(refreshTokenExpirationTime);

        String validToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(inputUser.getUuid().toString())
                .signWith(refreshTokenPrivateKey, SignatureAlgorithm.RS256)
                .compact();

        when(refreshTokenRedisTemplate.hasKey(inputUser.getUuid())).thenReturn(true);

        RefreshToken parsedToken = jwtService.parseRefreshToken(validToken);

        assertEquals(issuedTime.withNano(0), parsedToken.claims().iat());
        assertEquals(expirationTime.withNano(0), parsedToken.claims().exp());
        assertEquals(inputUser.getUuid(), parsedToken.claims().sub());
        verify(refreshTokenRedisTemplate, times(1)).hasKey(inputUser.getUuid());
    }
}