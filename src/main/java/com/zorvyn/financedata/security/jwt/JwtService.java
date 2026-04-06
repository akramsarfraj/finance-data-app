package com.zorvyn.financedata.security.jwt;

import com.zorvyn.financedata.exception.TokenExpiredException;
import com.zorvyn.financedata.security.UserPrinciple;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    Logger log = LoggerFactory.getLogger(JwtService.class);

    private String Secret = "11416371afya6381391931563171839131084tquetuq";

    @Value("${jwt.token.expire.limit}")
    private Long expireTime;

    /**
     * This method will generate JWT Token with 10 minute
     * Expiration time
     */
    public String generateToken(UserPrinciple principle) {
        Map<String,Object> claims = new HashMap<>();

        claims.put("id",principle.getId());
        claims.put("email",principle.getEmail());
        claims.put("userName",principle.getName());
        claims.put("role",principle.getRoleLabel());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(principle.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date( System.currentTimeMillis() + expireTime) )
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    /**
     * This method is used to extract claims
     */
    public Claims extractAllClaims(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException exception){
           throw new TokenExpiredException("token expired.");
        }
        return  claims;
    }

    public String extractUserName(String token) {
        Claims claims = extractAllClaims(token);
        if(claims != null){
            return claims.getSubject();
        }
        return  null;
    }
    public boolean validateToken(String token, UserDetails details) {

        String user = extractUserName(token);
        Boolean isExpire = extractAllClaims(token).getExpiration().before(new Date());


        return ( user.equals(details.getUsername()) && !isExpire );
    }


    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(Secret));
    }

}
