package com.pvt.SocialSips.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

public final class JwtParser {


    public static String extractSub(){
        Jwt jwt = getJwtFromSecurityContextHolder();
        return jwt.getClaim(JwtClaimNames.SUB);
    }

    private static Jwt getJwtFromSecurityContextHolder(){
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
