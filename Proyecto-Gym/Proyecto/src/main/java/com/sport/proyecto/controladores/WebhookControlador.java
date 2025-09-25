package com.sport.proyecto.controladores;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.sport.proyecto.entidades.Factura;
import com.sport.proyecto.servicios.FacturaServicio;
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
    @Autowired
    private FacturaServicio facturaServicio;


    @PostMapping
    public ResponseEntity<String> receiveWebhook(
            @RequestHeader("x-signature") String signature,
            @RequestHeader("x-request-id") String requestId,
            @RequestBody Map<String, Object> body) {

        try {
            // Verificamos HMAC
            boolean hmacValid = verifyHmac(signature, requestId, body);
            if (!hmacValid) {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            // HMAC válido → procesamos pago
            processPayment(body);

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    private boolean verifyHmac(String signature, String requestId, Map<String, Object> body) throws Exception {
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
            return false;
        }

        String manifest = "id:" + dataId + ";request-id:" + requestId + ";ts:" + ts + ";";
        String calculatedHash = utilServicio.generateHmacSHA256(pagoServicio.getWebhookSecret(), manifest);

        return calculatedHash.equals(hash);
    }

    private void processPayment(Map<String, Object> body) {
        try {
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            String paymentId = (String) data.get("id");

            // Obtenemos el pago desde Mercado Pago
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.valueOf(paymentId));

            if ("approved".equalsIgnoreCase(payment.getStatus())) {
                String facturaId = (String) payment.getMetadata().get("facturaId");
                facturaServicio.marcarComoPagada(facturaId);
            }
        } catch (Exception e) {
            // Loguear error
            e.printStackTrace();
        }
    }
}
