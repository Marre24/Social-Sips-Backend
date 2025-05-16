package com.pvt.SocialSips.user;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.pvt.SocialSips.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class FirebaseUserController {

    private final OidcUserDetailsService oidcUserDetailsService;

    @Autowired
    public FirebaseUserController(OidcUserDetailsService oidcUserDetailsService) {
        this.oidcUserDetailsService = oidcUserDetailsService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> authenticateFirebaseToken(OidcUserRequest oidcUserRequest, @RequestBody FirebaseToken firebaseToken) {

        OidcUser user = oidcUserDetailsService.loadUser(oidcUserRequest);
//        if (user == null) {
//
//            user = userService.register(new User(firebaseToken.getName(), firebaseToken.getUid(), List.of(new Role("OIDC_USER"), new Role("HOST"))));
//            List<GrantedAuthority> authorities = user.getRoles().stream()
//                    .map(role -> new SimpleGrantedAuthority(role.getName()))
//                    .collect(Collectors.toList());
//        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
