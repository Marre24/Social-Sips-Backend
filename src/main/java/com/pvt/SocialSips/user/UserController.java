package com.pvt.SocialSips.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<String> profile() {
        String name = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("name");
        return new ResponseEntity<>(name, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllQuestpools() {
        try {
            String id = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            var questpools = userService.getAllQuestpoolsBySub(id);
            return new ResponseEntity<>(questpools, HttpStatus.OK);
        } catch (Exception e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(){
        Object o  = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(o instanceof Jwt jwt){
            userService.getOrCreateUser(jwt);
            return ResponseEntity.ok("User found or created.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized request.");
    }
}
