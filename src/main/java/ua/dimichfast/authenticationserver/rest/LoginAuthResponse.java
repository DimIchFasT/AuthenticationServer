package ua.dimichfast.authenticationserver.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginAuthResponse {
    private SuccessFailure status;
    private String message;

    public enum SuccessFailure {
        SUCCESS, FAILURE
    }
}
