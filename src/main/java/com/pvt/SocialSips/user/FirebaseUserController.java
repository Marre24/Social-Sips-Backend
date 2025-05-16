package com.pvt.SocialSips.user;

import com.google.auth.oauth2.GoogleAuthUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.pvt.SocialSips.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class FirebaseUserController {

    private final UserService userService;

    @Autowired
    public FirebaseUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> authenticateFirebaseToken(@RequestBody FirebaseToken firebaseToken) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken.toString());

        List<Role> roles = List.of(new Role("HOST"), new Role("OIDC_USER"));

                PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
                decodedToken.getEmail(),
                        null,
                        roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(userService.getUserBySub(firebaseToken.getUid()) == null){
            User user = new User(firebaseToken.getName(),firebaseToken.getUid(), roles);
            userService.register(user);
        }


        return ResponseEntity.status(HttpStatus.OK).body("User authenticated.");
    }
}
