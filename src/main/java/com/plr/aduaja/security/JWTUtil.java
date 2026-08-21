package com.plr.aduaja.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class JWTUtil {

    // Kunci rahasia dari environment (jangan pernah hardcode di source!).
    // Jika JWT_SECRET tidak di-set, dibuat acak saat startup (token invalid
    // setiap restart) — aman secara default untuk lingkungan development.
    @Value("${jwt.secret:}")
    private String jwtSecret;

    // 1 hari = 86400000 ms
    private static final long EXPIRATION_DATE = 86400000L;

    private SecretKey cachedKey;

    // Menggunakan SecretKey (bukan Key biasa) dan menyertakan StandardCharsets
    private SecretKey getSigningKey() {
        if (cachedKey == null) {
            if (jwtSecret == null || jwtSecret.isBlank()) {
                cachedKey = generateRandomKey();
            } else {
                cachedKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            }
        }
        return cachedKey;
    }

    private SecretKey generateRandomKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("HmacSHA256 tidak tersedia di JVM ini", e);
        }
    }

    public String generateToken(String email, String role) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(email) // Menggantikan setSubject
                .claim("role", role)
                .issuedAt(new Date(now)) // Menggantikan setIssuedAt
                .expiration(new Date(now + EXPIRATION_DATE)) // Menggantikan setExpiration
                .signWith(getSigningKey()) // Algoritma HS256 otomatis terdeteksi dari SecretKey
                .compact();
    }

    // Tambahkan metode ini di dalam kelas JWTUtil Anda
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Menggunakan SecretKey internal JWTUtil
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
