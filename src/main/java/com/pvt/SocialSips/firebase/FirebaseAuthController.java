package com.pvt.SocialSips.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.pvt.SocialSips.role.Role;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class FirebaseAuthController {

    private final FirebaseAuth firebaseAuth;
    private final UserService userService;

    @Autowired
    public FirebaseAuthController(FirebaseAuth firebaseAuth, UserService userService) {
        this.firebaseAuth = firebaseAuth;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "/test-auth")
    public String getPrincipalName(Principal principal) {
        return principal.getName();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "/user-claims/{uid}")
    public void addAuthority(@PathVariable String uid, @RequestBody String authorityToAdd)
            throws FirebaseAuthException {

        Map<String, Object> currentClaims = firebaseAuth.getUser(uid).getCustomClaims();

        ArrayList<String> rolesOld =
                (ArrayList<String>) currentClaims.getOrDefault("authorities", List.of());
        Set<String> rolesNew = new HashSet<>(rolesOld);
        rolesNew.add(authorityToAdd);

        HashMap<String, Object> newClaims = new HashMap<>(currentClaims);
        newClaims.put("authorities", rolesNew);
        firebaseAuth.setCustomUserClaims(uid, newClaims);
    }

    @PostMapping("/token")
    public ResponseEntity<?> authenticateFirebaseToken(@RequestHeader String token)  throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token.replace("Token ", ""));
        List<Role> roles = List.of(new Role("HOST"), new Role("OIDC_USER"));

                PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
                decodedToken.getEmail(),
                        null,
                        roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(userService.getUserBySub(decodedToken.getUid()) == null){
            User user = new User(decodedToken.getName(),decodedToken.getUid(), roles);
            userService.register(user);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body("User authenticated.");
    }
}
