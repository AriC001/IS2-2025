package com.sport.proyecto.controladores;

import com.sport.proyecto.servicios.PagoServicio;
import com.sport.proyecto.servicios.UtilServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookControlador {

    @Autowired
    private PagoServicio pagoServicio;
    @Autowired
    private UtilServicio utilServicio;


    @PostMapping
    public ResponseEntity<String> receiveWebhook(
            @RequestHeader("x-signature") String signature,
            @RequestHeader("x-request-id") String requestId,
            @RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            String dataId = (String) data.get("id");

            // Sacamos ts y hash del header
            String[] parts = signature.split(",");
            String ts = null;
            String hash = null;
            for (String part : parts) {
                String[] kv = part.split("=");
                if (kv[0].trim().equals("ts")) ts = kv[1];
                if (kv[0].trim().equals("v1")) hash = kv[1];
            }

            if (ts == null || hash == null) {
                return ResponseEntity.status(401).body("Missing HMAC data");
            }

            String manifest = "id:" + dataId + ";request-id:" + requestId + ";ts:" + ts + ";";
            String calculatedHash = utilServicio.generateHmacSHA256(pagoServicio.getWebhookSecret(), manifest);

            if (calculatedHash.equals(hash)) {
                System.out.println("✅ HMAC verification passed");
                // Acá podrías buscar el pago con PaymentClient y validarlo
                return ResponseEntity.ok("OK");
            } else {
                System.out.println("❌ HMAC verification failed");
                return ResponseEntity.status(401).body("Unauthorized");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
