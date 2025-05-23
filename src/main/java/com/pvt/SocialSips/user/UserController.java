package com.pvt.SocialSips.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{sub}")
    public ResponseEntity<String> profile(@PathVariable String sub) {
        try {
            User user = userService.getUserBySub(sub);
            return new ResponseEntity<>(user.getFirstName(), HttpStatus.OK);
        } catch(EntityNotFoundException e){
            return new ResponseEntity<>("Could not find user with sub: " + sub, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/questpools/{sub}")
    public ResponseEntity<?> getAllQuestpools(@PathVariable String sub) {
        try {
            var questpools = userService.getAllQuestpoolsBySub(sub);
            return new ResponseEntity<>(questpools, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
