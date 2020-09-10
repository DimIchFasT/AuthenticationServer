package ua.dimichfast.authenticationserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.dimichfast.authenticationserver.rest.LoginAuthRequest;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("auth", new LoginAuthRequest());
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage() {
        return "success";
    }
}

