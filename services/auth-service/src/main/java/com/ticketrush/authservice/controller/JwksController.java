
package com.ticketrush.authservice.controller;

import com.ticketrush.authservice.security.JwtKeyProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
public class JwksController {

    private static final String KEY_ID = "auth-service-key-1";

    private final JwtKeyProvider keyProvider;

    public JwksController(JwtKeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        if (!(keyProvider.getPublicKey() instanceof RSAPublicKey publicKey)) {
            throw new IllegalStateException("JWKS endpoint requires an RSA public key");
        }

        Map<String, Object> jwk = Map.of(
                "kty", "RSA",
                "use", "sig",
                "alg", "RS256",
                "kid", KEY_ID,
                "n", encodeUnsignedInteger(publicKey.getModulus()),
                "e", encodeUnsignedInteger(publicKey.getPublicExponent()));

        return Map.of("keys", List.of(jwk));
    }

    private String encodeUnsignedInteger(BigInteger value) {
        byte[] bytes = value.toByteArray();

        if (bytes.length > 1 && bytes[0] == 0) {
            bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
        }

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
