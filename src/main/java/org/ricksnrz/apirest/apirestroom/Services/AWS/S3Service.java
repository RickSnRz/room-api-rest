package org.ricksnrz.apirest.apirestroom.Services.AWS;


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

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Sube un archivo a S3 con el número de DNI como nombre.
     *
     * @param file       archivo a subir (PDF).
     * @param dni        número de DNI del inquilino.
     * @return URL del archivo subido.
     * @throws IOException si ocurre un error al leer el archivo.
     */
    public String uploadFile(MultipartFile file, String dni) throws IOException {
        // Validar que el archivo sea un PDF
        if (!file.getContentType().equalsIgnoreCase("application/pdf")) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF.");
        }

        // Nombre del archivo será el DNI del inquilino
        String fileName = dni + ".pdf";

        try {
            // Subir el archivo a S3
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName) // Nombre del bucket
                            .key(fileName)      // Nombre del archivo en S3
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()) // Contenido del archivo
            );

            // Retornar la URL pública del archivo
            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

        } catch (S3Exception e) {
            // Manejar errores de AWS S3
            throw new RuntimeException("Error al subir el archivo a S3: " + e.awsErrorDetails().errorMessage());
        }
    }

    /**
     * Descarga un archivo PDF desde S3 usando el DNI como clave.
     *
     * @param dni número de DNI del inquilino.
     * @return flujo de entrada del archivo descargado.
     */
    public ResponseInputStream<GetObjectResponse> downloadFile(String dni) {
        // Nombre del archivo es el DNI con extensión .pdf
        String fileName = dni + ".pdf";

        try {
            return s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName) // Nombre del bucket
                            .key(fileName)      // Nombre del archivo en S3
                            .build()
            );
        } catch (S3Exception e) {
            // Manejar errores de AWS S3
            throw new RuntimeException("Error al descargar el archivo de S3: " + e.awsErrorDetails().errorMessage());
        }
    }
}
