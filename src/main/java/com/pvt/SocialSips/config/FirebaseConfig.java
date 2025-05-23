package com.pvt.SocialSips.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class FirebaseConfig {

    @Value("classpath:${social-sips-ec954-firebase-adminsdk-fbsvc-950d178d12.json}")
    private Resource resource;

    @PostConstruct
    public void onStart() {
        try {
            this.initialize();
        } catch (Exception e) {
            System.out.println("Error initializing FirebaseApp\n" + e);
        }
    }

    private void initialize() {
        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(resource.getInputStream())).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FirebaseApp.initializeApp(options);

    }
}

