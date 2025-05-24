package com.pvt.SocialSips.constants;

public final class WebConstants {

    public static final String JWT_ISSUER_URI = "https://securetoken.google.com/social-sips-ec954";
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

    public static final String[] ALLOWED_ORIGINS =
            {
                    "https://social-sips-ec954.web.app",
                    "https://social-sips-ec954.firebaseapp.com",
                    "ws://127.0.0.1:**/**",
                    "http://127.0.0.1:**/**",
                    "http://130.237.161.241**",
                    "http://localhost:50717",
            };

    public static final String[] PROTECTED_ENDPOINTS = {
            "/user", "/user/questpool",
            "/event/", "/event/start/",
            "/event", "/questpool",
            "/user/profile", "/questpool/",
    };

    public static final String[] PERMITTED_ENDPOINTS = {
            "/", "/home", "/css/**",
            "/error", "/__/hosting/**",
            "/event/join/**", "/user/login",
            "/.well-known/**", "/h2-console/**",
            "/questpool/standard"
    };


}
