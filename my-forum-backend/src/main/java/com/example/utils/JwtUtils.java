package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
@Component
public class JwtUtils {
    @Value("${spring.security.jwt.key}")
    private String key;
    @Value("${spring.security.jwt.expire}")
    private int expire;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // Generate token
    public String createJwt(UserDetails userDetails, int id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Calendar calendar = Calendar.getInstance();
        Date issueTime = calendar.getTime();
        Date expireTime = this.expireTime(calendar);
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", id)
                .withClaim("username", username)
                .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withIssuedAt(issueTime)
                .withExpiresAt(expireTime)
                .sign(algorithm);
    }

    // Parse header token
    public DecodedJWT resolveJwtFromHeaderToken(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) {
            return null;
        }
        return resolveJwtFromToken(token);
    }

    // Parse token
    public DecodedJWT resolveJwtFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            if (this.isInvalidatedToken(decodedJWT.getId())) {
                return null;
            }
            return decodedJWT.getExpiresAt().before(new Date()) ? null : decodedJWT;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    // Invalidate token
    public boolean invalidateJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) {
            return false;
        }
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = verifier.verify(token);
            String jwtId = verify.getId();
            Date expireTime = verify.getExpiresAt();
            return this.doInvalidate(jwtId, expireTime);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // Do invalidate token
    private boolean doInvalidate(String jwtId, Date expireTime) {
        if (isInvalidatedToken(jwtId)) {
            return false;
        }
        Date now = new Date();
        long timeout = Math.max(expireTime.getTime() - now.getTime(), 0);
        stringRedisTemplate.opsForValue().set(Const.JWT_BLACK_LIST_PREFIX + jwtId, "", timeout, TimeUnit.MILLISECONDS);
        return true;
    }

    // Check whether token is invalidated
    private boolean isInvalidatedToken(String jwtId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.JWT_BLACK_LIST_PREFIX + jwtId));
    }

    // Convert DecodedJWT to UserDetails
    public UserDetails toUserDetails(DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();
        return User
                .withUsername(claims.get("username").asString())
                .password("xxxx")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    // Get userId from DecodedJWT
    public int toUserId(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("id").asInt();
    }

    // Calculate expire time
    private Date expireTime(Calendar calendar) {
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }

    // Convert headerToken to a normal token
    private String convertToken(String headerToken) {
        if (headerToken == null || !headerToken.startsWith("Bearer ")) {
            return null;
        }
        return headerToken.substring(7);
    }
}
