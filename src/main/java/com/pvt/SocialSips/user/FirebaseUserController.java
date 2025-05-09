package com.pvt.SocialSips.user;

import com.google.firebase.auth.FirebaseToken;
import com.pvt.SocialSips.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/")
    public ResponseEntity<?> authenticateFirebaseToken(@RequestHeader String token, @AuthenticationPrincipal FirebaseToken firebaseToken) {

        User user = userService.getUserBySub(firebaseToken.getUid());
        if (user == null) {
            user = userService.register(new User(firebaseToken.getName(), firebaseToken.getUid(), List.of(new Role("OIDC_USER"), new Role("HOST"))));
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(user);

    }
}
