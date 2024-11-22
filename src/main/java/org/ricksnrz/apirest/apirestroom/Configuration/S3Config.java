package org.ricksnrz.apirest.apirestroom.Configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.region}")
    private String region; // La región de AWS

    @Value("${aws.access-key-id}")
    private String accessKeyId; // El Access Key

    @Value("${aws.secret-access-key}")
    private String secretAccessKey; // El Secret Key


    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region)) // Leer la región desde el archivo de propiedades
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                accessKeyId, // Acceder a la Access Key
                                secretAccessKey // Acceder a la Secret Key
                        )
                ))
                .build();
    }
}
