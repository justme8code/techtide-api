package com.justme8code.techtide.util;

import com.justme8code.techtide.exceptions.JWTException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthorizer {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationS}")
    private int jwtExpirationS;


    private Key key(){
        return
                Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Claims validateToken(String jwtToken) {
        try {
            String token = jwtToken.replace("Bearer ", "");
            SecretKey secretKey = new SecretKeySpec(key().getEncoded(), key().getAlgorithm());
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (Exception ex) {
            throw new JWTException(HttpStatus.NOT_ACCEPTABLE, "Invalid Authorization header", ex);
        }

    }



    public String generateToken(Authentication authentication) {
        String id = authentication.getName();  // Get the username (or other identifying info)

        // Get roles from Authentication object
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts
                .builder()
                .subject(id)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationS* 1000L))
                .signWith(key())
                .compact();
    }


    // Extract username from the token
    public String extractUsername(String token) {
        SecretKey secretKey = new SecretKeySpec(key().getEncoded(), key().getAlgorithm());
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    // Extract roles from the token and map to List<String>
    public List<String> extractRoles(String token) {
        SecretKey secretKey = new SecretKeySpec(key().getEncoded(), key().getAlgorithm());

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        // Extract roles and map to List<String>
        List<?> roles = claims.get("roles", List.class); // Get the roles as a List
        return roles.stream()
                .map(Object::toString) // Map each role to a String
                .toList(); // Collect the results into a List<String>
    }

    // Validate the token
    public boolean validateToken(String token, Authentication authentication) {
        String username = extractUsername(token);
        return (username.equals(authentication.getName()));
    }
}
