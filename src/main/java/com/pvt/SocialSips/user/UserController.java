package com.pvt.SocialSips.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(jwt.getClaim("name"), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@AuthenticationPrincipal Jwt jwt) {
        User u = userService.getUserBySub(jwt.getSubject());
        if (u == null) {
            u = new User(jwt.getClaim("name"), jwt.getSubject());
            userService.register(u);
        }
        return ResponseEntity.ok(u);
    }

    @GetMapping("/questpool")
    public ResponseEntity<?> getAllQuestpools(@AuthenticationPrincipal Jwt jwt) {

        try {
            var questpools = userService.getAllQuestpoolsBySub(jwt.getSubject());
            return new ResponseEntity<>(questpools, HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
