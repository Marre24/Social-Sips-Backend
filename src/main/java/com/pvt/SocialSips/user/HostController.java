package com.pvt.SocialSips.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "https://group-2-75.pvt.dsv.su.se/")
@RequestMapping("/user")
public class HostController {

    private final HostService hostService;

    @Autowired
    public HostController(HostService hostService) {
        this.hostService = hostService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(Host host) {
        hostService.register(host);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(Host host) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauth2) {
            String sub = oauth2.getPrincipal().getAttribute("sub");
            Host host = hostService.getUserBySub(sub);
            return ResponseEntity.ok(host);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthrorized user request.");
    }
}
