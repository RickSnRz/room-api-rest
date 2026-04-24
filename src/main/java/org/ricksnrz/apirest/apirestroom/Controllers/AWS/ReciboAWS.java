package org.ricksnrz.apirest.apirestroom.Controllers.AWS;

import org.ricksnrz.apirest.apirestroom.Services.AWS.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/recibos")
public class ReciboAWS{

    private final S3Service s3Service;

    public ReciboAWS(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Subir PDF de un recibo
     */
    @PostMapping("/{reciboId}/upload")
    public ResponseEntity<String> uploadRecibo(
            @PathVariable Long reciboId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String url = s3Service.uploadRecibo(file, reciboId);
        return ResponseEntity.ok("Recibo subido correctamente: " + url);
    }

    /**
     * Descargar PDF del recibo
     */
    @GetMapping("/{reciboId}/download")
    public ResponseEntity<byte[]> downloadRecibo(@PathVariable Long reciboId) {

        ResponseInputStream<GetObjectResponse> file = s3Service.downloadRecibo(reciboId);

        byte[] bytes;
        try {
            bytes = file.readAllBytes();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recibo-" + reciboId + ".pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }
}
