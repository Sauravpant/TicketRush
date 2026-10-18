package com.ticketrush.authservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtKeyProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtKeyProvider(@Value("${jwt.private-key-path}") Resource privateKeyResource,
            @Value("${jwt.public-key-path}") Resource publicKeyResource) throws Exception {
        this.privateKey = loadPrivateKey(privateKeyResource);
        this.publicKey = loadPublicKey(publicKeyResource);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private PrivateKey loadPrivateKey(Resource resource) throws Exception {
        String pem = readPemFile(resource, "PRIVATE KEY");
        byte[] decoded = Base64.getDecoder().decode(pem);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    private PublicKey loadPublicKey(Resource resource) throws Exception {
        String pem = readPemFile(resource, "PUBLIC KEY");
        byte[] decoded = Base64.getDecoder().decode(pem);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }

    private String readPemFile(Resource resource, String label) throws Exception {
        String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        return content.replace("-----BEGIN " + label + "-----", "").replace("-----END " + label + "-----", "")
                .replaceAll("\\s", "");
    }
}
