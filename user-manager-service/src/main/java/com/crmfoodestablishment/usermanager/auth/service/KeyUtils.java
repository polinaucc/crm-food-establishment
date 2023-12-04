package com.crmfoodestablishment.usermanager.auth.service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

    public static PrivateKey convertStringTokenToPrivateKey(
            String token,
            KeyFactory keyFactory
    ) throws InvalidKeySpecException {
        byte[] decodedRefreshTokenSecretKey = Base64.getDecoder().decode(token);
        var tokenSecretKeySpec = new PKCS8EncodedKeySpec(decodedRefreshTokenSecretKey);
        return keyFactory.generatePrivate(tokenSecretKeySpec);
    }

    public static PublicKey convertStringTokenToPublicKey(
            String token,
            KeyFactory keyFactory
    ) throws InvalidKeySpecException {
        byte[] decodedRefreshTokenPublicKey = Base64.getDecoder().decode(token);
        var tokenPublicKeySpec = new X509EncodedKeySpec(decodedRefreshTokenPublicKey);
        return keyFactory.generatePublic(tokenPublicKeySpec);
    }
}
