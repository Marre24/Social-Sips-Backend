package com.pvt.SocialSips.user;

import com.google.firebase.auth.FirebaseToken;
import com.pvt.SocialSips.firebase.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final FirebaseService firebaseService;

    @Autowired
    public UserController(UserService userService, FirebaseService firebaseService) {
        this.userService = userService;
        this.firebaseService = firebaseService;
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauth2) {
            String sub = oauth2.getPrincipal().getAttribute("sub");
            User user = userService.getUserBySub(sub);
            return new ResponseEntity<>(user.getFirstName(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Unauthorized user request!", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllQuestpools(@AuthenticationPrincipal FirebaseToken token) {
        try {
            String userUid = firebaseService.retrieveFirebaseAuth();
            User user = userService.getOrCreateUser(userUid);

            var questpools = userService.getAllQuestpoolsBySub(user.getSub());
            return new ResponseEntity<>(questpools, HttpStatus.OK);
        } catch (Exception e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
