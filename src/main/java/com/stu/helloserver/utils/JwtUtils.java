package com.stu.helloserver.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    // 自定义密钥（长度必须 >= 256位，本地测试用，生产环境请配置到环境变量）
    private static final String SECRET = "mySecretKey1234567890mySecretKey1234567890mySecretKey1234567890";
    // Token 有效期：1天（单位：毫秒）
    private static final long EXPIRATION = 86400000L;

    /**
     * 获取签名密钥（兼容 0.11.5 版本）
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * 生成 Token（0.11.5 标准写法）
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Token（0.11.5 标准写法，完全兼容）
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}