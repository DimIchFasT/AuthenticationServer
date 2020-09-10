package ua.dimichfast.authenticationserver.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${auth.jwt.tokenSecret}")
    private String tokenSecret;
    @Value("${auth.jwt.accessTokenExpirationMsec}")
    private long tokenExpirationMsec;
    @Value("${auth.jwt.refreshTokenExpirationMsec}")
    private Long refreshTokenExpirationMsec;

    public JwtTokenProvider(@Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        tokenSecret = Base64.getEncoder().encodeToString(tokenSecret.getBytes());
    }

    public JwtToken generateJwtToken(String username, Map<String, Object> claimsInfo, JwtToken.TokenType tokenType) {
        Date now = new Date();
        long duration = 0;

        if (tokenType == JwtToken.TokenType.ACCESS) {
            duration = now.getTime() + tokenExpirationMsec;
        }
        if (tokenType == JwtToken.TokenType.REFRESH) {
            duration = now.getTime() + refreshTokenExpirationMsec;
        }

        Date expirationDate = new Date(duration);

        Claims claims = Jwts.claims()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate);

        if (claimsInfo != null) {
            claims.putAll(claimsInfo);
        }

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
        return new JwtToken(tokenType, jwtToken, duration, LocalDateTime.ofInstant(expirationDate.toInstant(), ZoneId.systemDefault()));
    }

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token);
            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication getAuthenticationFromToken(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public LocalDateTime getExpiryDateFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
        return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }
}