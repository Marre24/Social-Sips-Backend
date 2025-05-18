package com.pvt.SocialSips.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.*;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.admin.sdk.path}")
    private Resource resource;

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(resource.getInputStream())).build();

            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.out.println(e.getCause() + e.getMessage());
        }
        return null;
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }

}

