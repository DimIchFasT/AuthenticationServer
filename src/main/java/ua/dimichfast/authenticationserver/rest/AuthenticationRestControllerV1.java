package ua.dimichfast.authenticationserver.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dimichfast.authenticationserver.model.User;
import ua.dimichfast.authenticationserver.repository.UserRepository;
import ua.dimichfast.authenticationserver.security.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//@RestController
//@RequestMapping("/api/v1/auth")
public class AuthenticationRestControllerV1 {
//
//    private final AuthenticationManager authenticationManager;
//    private UserRepository userRepository;
//    private JwtTokenProvider jwtTokenProvider;
//
//    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> authenticate(@RequestBody LoginAuthRequest request) {
//        return authenticateRequest(request);
//    }
//
//    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity<?> authenticateForm(LoginAuthRequest request) {
//        return authenticateRequest(request);
//    }
//
//    private ResponseEntity<?> authenticateRequest(LoginAuthRequest requestDTO) {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getUsernameOrEmail(), requestDTO.getPassword()));
//            User user = userRepository.findByEmailEqualsOrUsernameEquals(requestDTO.getUsernameOrEmail(), requestDTO.getUsernameOrEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
//            String token = jwtTokenProvider.generateAccessToken(requestDTO.getUsernameOrEmail(), user.getRole().getName());
//            Map<Object, Object> response = new HashMap<>();
//            response.put("email", requestDTO.getUsernameOrEmail());
//            response.put("token", token);
//            return ResponseEntity.ok(response);
//        } catch (AuthenticationException e) {
//            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
//        }
//    }
//
//    @PostMapping("/logout")
//    public void logout(HttpServletRequest request, HttpServletResponse response) {
//        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
//        securityContextLogoutHandler.logout(request, response, null);
//    }
}
