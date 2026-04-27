package org.ricksnrz.apirest.apirestroom.Controllers;

import org.ricksnrz.apirest.apirestroom.Entities.Recibo;
import org.ricksnrz.apirest.apirestroom.Services.AWS.S3Service;
import org.ricksnrz.apirest.apirestroom.Services.CRUD.ReciboService;
import org.ricksnrz.apirest.apirestroom.Services.WSP.WhatsAppService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final WhatsAppService whatsAppService;
    private final ReciboService reciboService;
    private final S3Service s3Service;

    @Value("${BUCKET_NAME}")
    private String bucketName;

    public WhatsAppController(WhatsAppService whatsAppService,
                              ReciboService reciboService,
                              S3Service s3Service) {
        this.whatsAppService = whatsAppService;
        this.reciboService = reciboService;
        this.s3Service = s3Service;
    }

    @PostMapping("/enviar-recibo/{reciboId}")
    public ResponseEntity<String> enviarRecibo(@PathVariable Long reciboId) {

        try {
            Recibo recibo = reciboService.obtenerReciboPorId(reciboId);

            // 📌 URL DEL PDF EN S3
            String url = "https://" + bucketName + ".s3.amazonaws.com/recibos/" + reciboId + ".pdf";

            String mensaje = "Hola " + recibo.getInquilino().getNombre() + " 👋\n\n"
                    + "Tu recibo de alquiler ya está disponible.\n"
                    + "Monto: S/ " + recibo.getMonto() + "\n"
                    + "Fecha: " + recibo.getFechaEmision() + "\n\n"
                    + "Te adjunto tu recibo en PDF.";

            // ⚠️ IMPORTANTE: formato internacional
            String numero = "+51" + recibo.getInquilino().getTelefono();

            whatsAppService.enviarRecibo(numero, mensaje, url);

            return ResponseEntity.ok("Mensaje enviado por WhatsApp");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al enviar mensaje");
        }
    }
}