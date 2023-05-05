package com.projetjavaopc.api.tools.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.projetjavaopc.api.models.Users;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;


/**
 * Class implementing various utility methods for JWT authentication
 */
@Component
public class JwtTokenProvider {

    /**
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    @Value("${secret.token.key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInSeconds = 3600; // 1h

    @Autowired
    private UserDetailsService myUserDetailsService;

    /**
     * Hook to encode the secret key using Base64
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    /**
     * Create a token for the given email
     *
     * @param email An email
     * @return A JWT token
     */
    public String createToken(Users user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .claim("id", user.getId())
                .claim("username", user.getName())
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract a token from the HTTP headers
     *
     * @param req An HTTP request
     * @return The authorization token, or null
     */
    public String extractToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

        /**
     * Extract a token from given auth
     *
     * @param req An HTTP request
     * @return The authorization token, or null
     */
    public String extractBearer(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }



    /**
     * Validate a JWT token
     *
     * @param token A token
     * @return The token claims
     * @throws JwtException when an error occurs while validating the token
     */
    public Jws<Claims> validateToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }




    /**
     * Create an authentication context using the content of the JWT token
     *
     * @param claims The JWT claims
     * @return An authentication context
     */
    public Authentication getAuthentication(Jws<Claims> claims) {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(claims.getBody().getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}

