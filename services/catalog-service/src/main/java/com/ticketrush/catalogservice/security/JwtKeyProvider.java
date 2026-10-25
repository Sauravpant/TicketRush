package com.ticketrush.catalogservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
public class JwtKeyProvider {

    private final PublicKey publicKey;

    public JwtKeyProvider(@Value("${jwt.jwks-uri}") String jwksUri) {
        this.publicKey = fetchPublicKey(jwksUri);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @SuppressWarnings("unchecked")
    private PublicKey fetchPublicKey(String jwksUri) {
        RestClient restClient = RestClient.create();
        Map<String, Object> jwks = restClient.get()
                .uri(jwksUri)
                .retrieve()
                .body(Map.class);

        List<Map<String, String>> keys = (List<Map<String, String>>) jwks.get("keys");
        Map<String, String> key = keys.getFirst();

        BigInteger modulus = new BigInteger(1, decode(key.get("n")));
        BigInteger exponent = new BigInteger(1, decode(key.get("e")));

        try {
            return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to build RSA public key from JWKS", e);
        }
    }

    private byte[] decode(String base64Url) {
        return Base64.getUrlDecoder().decode(base64Url);
    }
}