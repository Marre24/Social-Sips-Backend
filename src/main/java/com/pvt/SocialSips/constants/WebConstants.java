package com.pvt.SocialSips.constants;

import org.springframework.beans.factory.annotation.Value;

public final class WebConstants {

        public static final String[] PROTECTED_ENDPOINTS = {"/user/questpools", "/event", "/event/start", "/questpool/**", "/user"};

        public static final String[] PERMITTED_ENDPOINTS = {"/", "/home", "/css/**", "/error", "/__/hosting/**",
                                                            "/ws/**", "/event/join/**", "/token", "/.well-known/**",
                                                            "/h2-console/**","/questpool/standard"};

        public static final String[] ALLOWED_ORIGINS = {"https://social-sips-ec954.web.app",
                                                            "https://social-sips-ec954.firebaseapp.com",
                                                            "ws://127.0.0.1:5000/"};

        @Value("${WEB_API_KEY}")
        public static String WEB_API_KEY;
}
