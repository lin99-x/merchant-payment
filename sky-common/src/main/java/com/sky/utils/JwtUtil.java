package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * Generate JWT token
     * Use HS256 algorithm to sign the token, and set the expiration time and claims
     *
     * @param secretKey JWT secret key
     * @param ttlMillis JWT expiration time in milliseconds
     * @param claims    JWT claims
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // Algorithm for signing the JWT, using the HS256 algorithm and the provided secret key
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // Calculate the expiration time of the JWT, which is the current time plus the provided ttlMillis
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // Build the JWT token using the Jwts.builder() method
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Parse JWT token
     *
     * @param secretKey JWT secret key
     * @param token     JWT token to be parsed
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // Parse the JWT token using the Jwts.parser() method, setting the signing key and parsing the token
        Claims claims = Jwts.parser()
                // Set the signing key for verifying the JWT token, using the provided secret key
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // Parse the JWT token and get the claims from the token
                .parseClaimsJws(token).getBody();
        return claims;
    }

}
