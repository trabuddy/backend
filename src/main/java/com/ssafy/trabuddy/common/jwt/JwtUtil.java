package com.ssafy.trabuddy.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.accessExpiration}")
    private long accessExpiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    public String generateToken(long socialId) {
        Claims claims = Jwts.claims()
                .add("socialId", socialId)
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .subject("kimandjang")
                .compact();
    }

    public String generateRefreshToken(long socialId) {
        Claims claims = Jwts.claims()
                .add("socialId", socialId)
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .subject("kimandjang")
                .compact();
    }

    public Claims parse(String token, HttpServletResponse response) throws IOException {
        try {
            return (Claims) Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parse(token)
                    .getPayload();

        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("올바르지 않은 형식의 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원하지 않는 토큰입니다.");
        } catch (PrematureJwtException ex) {
            throw new JwtException("승인되지 않은 토큰입니다.");
        }
    }
}
