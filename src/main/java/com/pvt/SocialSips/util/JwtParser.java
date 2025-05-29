package com.pvt.SocialSips.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public final class JwtParser {

    public static String extractSub(){
        Jwt jwt = getJwtFromSecurityContextHolder();
        return jwt.getClaim("sub");
    }

    private static Jwt getJwtFromSecurityContextHolder(){
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}