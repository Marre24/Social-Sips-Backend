package com.pvt.SocialSips.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;

@Service
public class FirebaseConfig {

    @Value("${firebase.admin.sdk.path}")
    private File file;

    @PostConstruct
    public void onStart() {
        try {
            this.initialize();
        } catch (Exception e) {
            System.out.println("Error initializing FirebaseApp\n" + e);
        }
    }

    private void initialize() {
        if (FirebaseApp.getApps().isEmpty() || FirebaseApp.getApps() == null) {
            try (InputStream inputStream = new DataInputStream(new FileInputStream(file))) {
                FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(inputStream)).build();

                FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                System.out.println(e.getCause() + e.getMessage());
            }
        }
    }
}

