package com.pvt.SocialSips.user;

import com.pvt.SocialSips.questpool.Questpool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<?> getAllQuestpools(@AuthenticationPrincipal DefaultOidcUser defaultOidcUser) {
        try {
            String sub = defaultOidcUser.getSubject();
            var questpools = userService.getAllQuestpoolsBySub(sub);
            return new ResponseEntity<>(questpools, HttpStatus.OK);
        } catch (Exception e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
