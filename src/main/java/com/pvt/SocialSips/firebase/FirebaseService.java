package com.pvt.SocialSips.firebase;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FirebaseService {

    public String retrieveFirebaseAuth(){
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElseThrow(IllegalStateException::new);
    }

    // TOTO: Additional token expiration/retrieval/renewal service functions with FirebaseRefreshTokens
}
