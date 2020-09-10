package ua.dimichfast.authenticationserver.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.dimichfast.authenticationserver.model.User;
import ua.dimichfast.authenticationserver.repository.UserRepository;
import ua.dimichfast.authenticationserver.security.jwt.JwtToken;
import ua.dimichfast.authenticationserver.security.jwt.JwtTokenProvider;
import ua.dimichfast.authenticationserver.security.util.CookieUtil;

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final CookieUtil cookieUtil;

    public AuthService(UserRepository userRepository, JwtTokenProvider tokenProvider, CookieUtil cookieUtil) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.cookieUtil = cookieUtil;
    }

    public ResponseEntity<LoginAuthResponse> login(LoginAuthRequest loginRequest, String accessToken, String refreshToken) {
        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        User user = userRepository.findByEmailEqualsOrUsernameEquals(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new IllegalArgumentException("User not found with loginOrEmail " + usernameOrEmail));

        boolean accessTokenValid = tokenProvider.validateToken(accessToken);
        boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);

        HttpHeaders responseHeaders = new HttpHeaders();
        JwtToken newAccessToken;
        JwtToken newRefreshToken;
        Map<String, Object> claimsInfo = Map.of("role", user.getRole().getName(),
                "permissions", user.getRole().getPermissions(),
                "userStatus", user.getStatus());

        if (!accessTokenValid && !refreshTokenValid) {
            newAccessToken = tokenProvider.generateJwtToken(user.getUsername(), claimsInfo, JwtToken.TokenType.ACCESS);
            newRefreshToken = tokenProvider.generateJwtToken(user.getUsername(), null, JwtToken.TokenType.REFRESH);
            addAccessTokenCookie(responseHeaders, newAccessToken);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);
        }

        if (!accessTokenValid && refreshTokenValid) {
            newAccessToken = tokenProvider.generateJwtToken(user.getUsername(), claimsInfo, JwtToken.TokenType.ACCESS);
            addAccessTokenCookie(responseHeaders, newAccessToken);
        }

        if (accessTokenValid && refreshTokenValid) {
            newAccessToken = tokenProvider.generateJwtToken(user.getUsername(), claimsInfo, JwtToken.TokenType.ACCESS);
            newRefreshToken = tokenProvider.generateJwtToken(user.getUsername(), null, JwtToken.TokenType.REFRESH);
            addAccessTokenCookie(responseHeaders, newAccessToken);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);
        }
        addAccessControlHeaders(responseHeaders);
        LoginAuthResponse loginResponse = new LoginAuthResponse(LoginAuthResponse.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    public ResponseEntity<LoginAuthResponse> refresh(String accessToken, String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token is invalid!");
        }

        String currentUserEmail = tokenProvider.getUsernameFromToken(accessToken);
        User user = userRepository.findByEmailEqualsOrUsernameEquals(currentUserEmail, currentUserEmail).orElseThrow(() -> new IllegalArgumentException("User not found with loginOrEmail " + currentUserEmail));
        Map<String, Object> claimsInfo = Map.of("role", user.getRole(),
                "permissions", user.getRole().getPermissions(),
                "userStatus", user.getStatus());

        JwtToken newAccessToken = tokenProvider.generateJwtToken(currentUserEmail, claimsInfo, JwtToken.TokenType.ACCESS);
        HttpHeaders responseHeaders = new HttpHeaders();
        addAccessTokenCookie(responseHeaders,newAccessToken);
//        responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration()).toString());
        addAccessControlHeaders(responseHeaders);
        LoginAuthResponse loginResponse = new LoginAuthResponse(LoginAuthResponse.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }


    private void addAccessTokenCookie(HttpHeaders httpHeaders, JwtToken token) {
        System.out.println(token.getTokenValue());
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, JwtToken token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    private void addAccessControlHeaders(HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
    }
}
