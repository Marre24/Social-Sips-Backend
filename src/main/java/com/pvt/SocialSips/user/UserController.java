package com.pvt.SocialSips.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pvt.SocialSips.util.JwtParser.extractSub;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> profile() {
        try {
            User user = userService.getUserBySub(extractSub());
            return new ResponseEntity<>(user.getFirstName(), HttpStatus.OK);
        } catch(EntityNotFoundException e){
            return new ResponseEntity<>("Could not find user with sub: " + extractSub(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/questpools")
    public ResponseEntity<?> getAllQuestpools() {
        try {
            var questpools = userService.getAllQuestpoolsBySub(extractSub());
            return new ResponseEntity<>(questpools, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login/{sub}")
    public ResponseEntity<String> login(@PathVariable String sub) {
        User user = userService.getOrCreateUser(new User("username", sub));

        return ResponseEntity.ok("User: " + user + " found or created.");

    }
}
