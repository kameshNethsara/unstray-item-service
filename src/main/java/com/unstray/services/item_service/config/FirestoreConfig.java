package com.unstray.services.item_service.config;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore firestore() throws IOException {
        // GCP credentials will automatically be picked up from Application Default Credentials (ADC)
        // or environment variables when deployed on Google Cloud Platform.
        FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance();
        return firestoreOptions.getService();
    }
}

//package com.unstray.services.item_service.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.FirestoreOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//public class FirestoreConfig {
//
//    @Value("${gcp.firestore.project-id}")
//    private String projectId;
//
//    @Bean
//    public Firestore firestore() throws IOException {
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
//                .setProjectId(projectId)
//                .build();
//        return firestoreOptions.getService();
//    }
//}