package ua.dimichfast.authenticationserver.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginAuthRequest {
    private String usernameOrEmail;
    private String password;
}
