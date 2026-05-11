package com.sajithjeewantha.auth_service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JwtSecretMaker {

    @Test
    public void generateTokenV1() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64 = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(base64);
    }

    @Test
    public void generateTokenV2() {
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGenerator.generateKey();
            String secretKey2 = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println(secretKey2);
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException();
        }
    }

    @Test
    public void generateTokenV3() {
        SecretKey key = Jwts.SIG.HS512.key().build();
        String encodedKey =  DatatypeConverter.printBase64Binary(key.getEncoded());
        System.out.printf(encodedKey);
    }
}
