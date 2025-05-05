package com.pvt.SocialSips.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(User user) {
        userService.register(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(User user) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauth2) {
            String sub = oauth2.getPrincipal().getAttribute("sub");
            User user = userService.getUserBySub(sub);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthrorized user request.");
    }
}
