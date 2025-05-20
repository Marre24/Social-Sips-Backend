package com.pvt.SocialSips.constants;

import org.springframework.beans.factory.annotation.Value;

public final class WebConstants {

        public static final String[] PROTECTED_ENDPOINTS = {"/user/**", "/event/", "/event/start/", "/questpool/**"};

        public static final String[] PERMITTED_ENDPOINTS = {"/", "/home", "/css/**", "/error", "/__/hosting/**",
                                                            "/event/join/**", "/auth/token", "/.well-known/**",
                                                            "/h2-console/**"};

        public static final String[] ALLOWED_ORIGINS = {"https://social-sips-ec954.web.app",
                                                            "https://social-sips-ec954.firebaseapp.com",
                                                            "ws://127.0.0.1:5000/"};

        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String USER_ID_CLAIM = "user_id";
        public static final String INVALID_CREDENTIALS_ERROR = "INVALID_LOGIN_CREDENTIALS";
        public static final String SIGN_IN_BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";
        @Value("${WEB_API_KEY}")
        public static String WEB_API_KEY;
}
