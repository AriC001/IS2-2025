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
            @RequestHeader(value = "x-signature", required = false) String signature,
            @RequestHeader(value = "x-request-id", required = false) String requestId,
            @RequestBody Map<String, Object> body) {

        try {
            // Verificamos HMAC
            //boolean hmacValid = verifyHmac(signature, requestId, body);
            //if (!hmacValid) {
            //    return ResponseEntity.status(401).body("Unauthorized");
            //}

            // HMAC válido → procesamos pago
            processPayment(body);

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    private boolean verifyHmac(String signature, String requestId, Map<String, Object> body) throws Exception {
        Map<String, Object> data = (Map<String, Object>) body.get("data");
        String dataId = String.valueOf(data.get("id"));

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
            System.out.println("WEBHOOK BODY: " + body);
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            String resource = (String) body.get("resource");
            String paymentId = null;
            if (data != null) {
                paymentId = String.valueOf(data.get("id"));
            } else if (resource != null) {
                // Si el resource es solo un número, lo tomamos como paymentId
                paymentId = resource.contains("/") ? resource.substring(resource.lastIndexOf("/") + 1) : resource;
            }

            // Obtenemos el pago desde Mercado Pago
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.valueOf(paymentId));

            if ("approved".equalsIgnoreCase(payment.getStatus())) {
                String facturaId = payment.getExternalReference();
                if (facturaId == null && payment.getMetadata() != null) {
                    facturaId = (String) payment.getMetadata().get("facturaId");
                }
                facturaServicio.marcarComoPagada(facturaId);
            }
            if ("pending".equalsIgnoreCase(payment.getStatus())) {
                String facturaId = payment.getExternalReference();
                if (facturaId == null && payment.getMetadata() != null) {
                    facturaId = (String) payment.getMetadata().get("facturaId");
                }
                facturaServicio.marcarComoPendiente(facturaId);
            }
        } catch (Exception e) {
            // Loguear error
            e.printStackTrace();
        }
    }
}
