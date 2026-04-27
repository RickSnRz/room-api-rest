package org.ricksnrz.apirest.apirestroom.Services.AWS;


import org.ricksnrz.apirest.apirestroom.Entities.Recibo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;


    @Value("${BUCKET_NAME}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    private void validarPdf(MultipartFile file) {
        if (!file.getContentType().equalsIgnoreCase("application/pdf")) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF.");
        }
    }

    // =========================
    // DNI DE INQUILINOS
    // =========================
    public String uploadDni(MultipartFile file, String dni) throws IOException {
        validarPdf(file);

        String key = "dni/" + dni + ".pdf";

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType("application/pdf")
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    public ResponseInputStream<GetObjectResponse> downloadDni(String dni) {
        String key = "dni/" + dni + ".pdf";

        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        );
    }

    // =========================
    // RECIBOS
    // =========================
    public String uploadRecibo(MultipartFile file, Long reciboId) throws IOException {
        validarPdf(file);

        String key = "recibos/" + reciboId + ".pdf";

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType("application/pdf")
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    public ResponseInputStream<GetObjectResponse> downloadRecibo(Long reciboId) {
        String key = "recibos/" + reciboId + ".pdf";

        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        );
    }
}
