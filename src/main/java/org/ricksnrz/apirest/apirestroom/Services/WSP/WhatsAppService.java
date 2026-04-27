package org.ricksnrz.apirest.apirestroom.Services.WSP;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Arrays;

@Service
public class WhatsAppService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String from;

    public void enviarRecibo(String numeroDestino, String mensaje, String urlPdf) {

        Twilio.init(accountSid, authToken);



        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:" + numeroDestino),
                        new com.twilio.type.PhoneNumber(from),
                        mensaje
                )
                .setMediaUrl(Arrays.asList(URI.create(urlPdf))) // 🔥 AQUÍ VA EL PDF
                .create();

    }
}