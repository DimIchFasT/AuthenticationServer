package ua.dimichfast.authenticationserver.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${auth.jwt.header}")
    private String authorizationHeader;

    @Value("${auth.jwt.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${auth.jwt.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtToken(httpServletRequest);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthenticationFromToken(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(e.getHttpStatus().value());
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtToken(HttpServletRequest request) {
        String token = getJwtFromRequest(request);
        if (token == null) {
            token = getJwtFromCookie(request);
        }
        return token;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(authorizationHeader);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.replace("Bearer ", "");
        }
        return null;
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (accessTokenCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}