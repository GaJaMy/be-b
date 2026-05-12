package com.liveclass.be_b.security.jwt;

import com.liveclass.be_b.security.AuthenticatedPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey(String principalType) {
        return Keys.hmacShaKeyFor(getTokenProperties(principalType).getSecret().getBytes(StandardCharsets.UTF_8));
    }

    private String generateAccessToken(Long principalId, String username, String role, String principalType) {
        return Jwts.builder()
                .subject(username)
                .claim("principalId", principalId)
                .claim("role", role)
                .claim("principalType", principalType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + getAccessTokenExpiry(principalType)))
                .signWith(getSigningKey(principalType))
                .compact();
    }

    private Claims parseClaimsWithSigningKey(String token, String principalType) {
        return Jwts.parser()
                .verifyWith(getSigningKey(principalType))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims parseClaims(String token) {
        JwtException lastJwtException = null;
        IllegalArgumentException lastIllegalArgumentException = null;

        try {
            return parseClaimsWithSigningKey(token, AuthenticatedPrincipal.ADMIN_PRINCIPAL_TYPE);
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException e) {
            lastJwtException = e;
        } catch (IllegalArgumentException e) {
            lastIllegalArgumentException = e;
        }

        try {
            return parseClaimsWithSigningKey(token, AuthenticatedPrincipal.CREATOR_PRINCIPAL_TYPE);
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException e) {
            if (lastJwtException != null) {
                throw lastJwtException;
            }
            throw e;
        } catch (IllegalArgumentException e) {
            if (lastIllegalArgumentException != null) {
                throw lastIllegalArgumentException;
            }
            throw e;
        }
    }

    public long getAccessTokenExpiry(String principalType) {
        return getTokenProperties(principalType).getAccessTokenExpiry();
    }

    private JwtProperties.TokenProperties getTokenProperties(String principalType) {
        if (AuthenticatedPrincipal.ADMIN_PRINCIPAL_TYPE.equals(principalType)) {
            return jwtProperties.getAdmin();
        }
        if (AuthenticatedPrincipal.CREATOR_PRINCIPAL_TYPE.equals(principalType)) {
            return jwtProperties.getCreator();
        }
        throw new IllegalArgumentException("지원하지 않는 principalType 입니다: " + principalType);
    }
}
