package org.ricksnrz.apirest.apirestroom.Controllers.AWS;


import org.ricksnrz.apirest.apirestroom.Services.AWS.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/inquilinos")
public class InquilinoAWS {

    private final S3Service s3Service;

    @Autowired
    public InquilinoAWS(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Endpoint para subir un archivo PDF del DNI del inquilino
     *
     * @param dni  Número de DNI del inquilino
     * @param file Archivo PDF que se subirá
     * @return URL del archivo subido
     * @throws IOException en caso de error al subir el archivo
     */
    @PostMapping("/{dni}/upload")
    public ResponseEntity<String> uploadDni(@PathVariable String dni, @RequestParam("file") MultipartFile file) throws IOException {
        // Subir archivo PDF con el número de DNI como nombre
        String fileUrl = s3Service.uploadFile(file, dni);

        // Responder con la URL del archivo subido
        return ResponseEntity.ok("Archivo subido correctamente: " + fileUrl);
    }

    /**
     * Endpoint para descargar el archivo PDF del DNI del inquilino
     *
     * @param dni Número de DNI del inquilino
     * @return El archivo PDF como respuesta
     */
    @GetMapping("/{dni}/download")
    public ResponseEntity<byte[]> downloadDni(@PathVariable String dni) {
        // Descargar archivo desde S3
        ResponseInputStream<GetObjectResponse> file = s3Service.downloadFile(dni);

        // Leer el contenido del archivo PDF
        byte[] fileBytes;
        try {
            fileBytes = file.readAllBytes();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }

        // Definir los encabezados para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + dni + ".pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        // Retornar el archivo como respuesta para descarga
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }
}
