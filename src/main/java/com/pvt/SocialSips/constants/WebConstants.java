package com.pvt.SocialSips.constants;

import org.springframework.beans.factory.annotation.Value;

public final class WebConstants {

    public static final String[] PROTECTED_ENDPOINTS = {"/user", "/user/questpools", "/event", "/event/start", "/event/guests", "/questpool/**"};

    public static final String[] PERMITTED_ENDPOINTS = {"/", "/home", "/css/**", "/error", "/__/hosting/**", "/h2-console/**",  "/.well-known/**",
            "/event/join/**", "/event/questpools/**", "/event/started/**", "/event/guests/*/*",
            "/questpool/standard", "/token"
    };

    public static final String[] ALLOWED_ORIGINS = {"https://social-sips-ec954.web.app",
            "https://social-sips-ec954.firebaseapp.com",
    };

    @Value("${WEB_API_KEY}")
    public static String WEB_API_KEY;
}