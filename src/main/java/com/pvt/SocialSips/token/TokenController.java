package com.pvt.SocialSips.token;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.pvt.SocialSips.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {
    private static final Logger LOG = LoggerFactory.getLogger(TokenController.class);

    private final TokenService tokenService;

    public TokenController(TokenService tokenService){
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> token(@RequestBody String googleIdToken) {

        GoogleIdToken.Payload payload = tokenService.verifyGoogleIdToken(googleIdToken);
        if (payload == null) {
            return new ResponseEntity<>("Invalid Google access token", HttpStatus.UNAUTHORIZED);
        }

        String token = tokenService.generateToken(new User(payload.getSubject(), payload.getSubject()));
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


}