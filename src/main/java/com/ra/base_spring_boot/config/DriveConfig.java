package com.ra.base_spring_boot.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class DriveConfig {
    private static  final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    @Bean
    public Drive createDriveCredentials() throws IOException, GeneralSecurityException {

        InputStream inputStream = new ClassPathResource("drive-config.json").getInputStream();
        GoogleCredentials credential = GoogleCredentials.fromStream(inputStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credential))
                .build();
    }
}
