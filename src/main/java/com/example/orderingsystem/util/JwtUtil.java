/**
 * Restaurant Ordering System - JWT Utility
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.util;

import com.example.orderingsystem.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类
 */
@Component
@Slf4j
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            String username = getClaimFromToken(token, Claims::getSubject);
            log.debug("从令牌中获取用户名: {}", username);
            return username;
        } catch (ExpiredJwtException e) {
            log.warn("令牌已过期: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            log.warn("令牌签名验证失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("从令牌中获取用户名时发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 从令牌中获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * 从令牌中获取指定数据
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * 从令牌中获取所有数据
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("解析令牌时发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 检查令牌是否过期
     */
    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            boolean isExpired = expiration.before(new Date());
            if (isExpired) {
                log.debug("令牌已过期");
            }
            return isExpired;
        } catch (Exception e) {
            log.error("检查令牌是否过期时发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 为用户生成令牌
     */
    public String generateToken(User user) {
        try {
            log.info("为用户 {} 生成令牌, 角色: {}", user.getUsName(), user.getRole());
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("role", user.getRole());
            String token = doGenerateToken(claims, user.getUsName());
            log.debug("令牌生成成功");
            return token;
        } catch (Exception e) {
            log.error("生成令牌时发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 生成令牌
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    /**
     * 验证令牌
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            log.debug("验证令牌: 用户名={}, 是否有效={}", username, isValid);
            return isValid;
        } catch (Exception e) {
            log.error("验证令牌时发生异常: {}", e.getMessage(), e);
            return false;
        }
    }
} 