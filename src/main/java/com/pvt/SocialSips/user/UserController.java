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
@CrossOrigin(origins = "https://group-2-75.pvt.dsv.su.se")
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
    public ResponseEntity<Set<Questpool>> getAllQuestpools(@AuthenticationPrincipal DefaultOidcUser defaultOidcUser) {
        String sub = defaultOidcUser.getSubject();

        return new ResponseEntity<>(userService.getAllQuestpoolsBySub(sub), HttpStatus.OK);
    }
}
