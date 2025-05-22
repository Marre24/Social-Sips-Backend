package com.pvt.SocialSips.token;

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
    public ResponseEntity<String> token(@RequestBody TokenRequestBody tokenRequestBody) {

        String accessToken = tokenRequestBody.accessToken();
        //TODO verify access token

        User user = tokenRequestBody.user();
        LOG.debug("Token requested for user: '{}'", user.getSub());
        String token = tokenService.generateToken(user);
        LOG.debug("Token granted {}", token);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
