package ua.dimichfast.authenticationserver.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ua.dimichfast.authenticationserver.rest.AuthService;
import ua.dimichfast.authenticationserver.rest.LoginAuthRequest;
import ua.dimichfast.authenticationserver.rest.LoginAuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginAuthResponse> login(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestBody LoginAuthRequest loginRequest
    ) {
        System.out.println(accessToken);
        System.out.println(refreshToken);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authService.login(loginRequest, accessToken, refreshToken);
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginAuthResponse> refreshToken(@CookieValue(name = "accessToken", required = false) String accessToken,
                                                          @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        return authService.refresh(accessToken, refreshToken);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
