package com.danielpietka.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtUtil {
    private final String tokenSecret;
    private final int tokenExpiration;
    private static final String ALGORITHM = "HmacSHA256";
    private static final int MILLISECONDS_IN_SECOND = 1000;

    public JwtUtil(String tokenSecret, int tokenExpiration) {
        this.tokenSecret = tokenSecret;
        this.tokenExpiration = tokenExpiration;
    }

    public String generateToken(String username) {
        String header = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));

        long now = System.currentTimeMillis();
        String payload = String.format(
                "{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}",
                username,
                now / MILLISECONDS_IN_SECOND,
                (now + tokenExpiration) / MILLISECONDS_IN_SECOND
        );

        String payloadEncoded = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        String signature = generateSignature(header + "." + payloadEncoded);

        return header + "." + payloadEncoded + "." + signature;
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];

            String computedSignature = generateSignature(header + "." + payload);
            if (!computedSignature.equals(signature)) {
                return false;
            }

            String payloadDecoded = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
            long expiration = Long.parseLong(payloadDecoded.replaceAll(".*\"exp\":(\\d+),?.*", "$1"));
            return System.currentTimeMillis() / MILLISECONDS_IN_SECOND < expiration;
        } catch (Exception e) {
            return false;
        }
    }

    private String generateSignature(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(
                    tokenSecret.getBytes(StandardCharsets.UTF_8),
                    ALGORITHM
            );
            sha256_HMAC.init(secretKey);

            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating signature", e);
        }
    }
}
