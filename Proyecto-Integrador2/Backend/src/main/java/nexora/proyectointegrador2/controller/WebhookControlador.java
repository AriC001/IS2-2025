package nexora.proyectointegrador2.controller;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


// import com.sport.proyecto.entidades.Factura;
// import com.sport.proyecto.servicios.FacturaServicio;
// import com.sport.proyecto.servicios.PagoServicio;
// import com.sport.proyecto.servicios.UtilServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookControlador {

    // @Autowired
    // private PagoServicio pagoServicio;
    // @Autowired
    // private UtilServicio utilServicio;
    // @Autowired
    // private FacturaServicio facturaServicio;

    @Value("${mercadopago.access-token}")
    private String mercadopagoAccessToken;


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

    /**
     * Llama al resource de merchant_orders y procesa los pagos asociados.
     */
    private void fetchMerchantOrderAndProcessPayments(String resourceUrl) {
        try {
            System.out.println("Fetching merchant order: " + resourceUrl);
            java.net.http.HttpClient http = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest req = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(resourceUrl))
                    .header("Authorization", "Bearer " + mercadopagoAccessToken)
                    .GET()
                    .build();

            java.net.http.HttpResponse<String> resp = http.send(req, java.net.http.HttpResponse.BodyHandlers.ofString());
            System.out.println("Merchant order response status: " + resp.statusCode());
            System.out.println("Merchant order response body: " + resp.body());

            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(resp.body());
                com.fasterxml.jackson.databind.JsonNode paymentsNode = root.path("payments");
                if (paymentsNode.isArray()) {
                    for (com.fasterxml.jackson.databind.JsonNode p : paymentsNode) {
                        String pid = null;
                        if (p.has("id")) pid = p.get("id").asText();
                        else if (p.isTextual()) pid = p.asText();
                        if (pid != null && !pid.isBlank()) {
                            try {
                                PaymentClient client = new PaymentClient();
                                Payment payment = client.get(Long.valueOf(pid));
                                System.out.println("Procesando pago id desde merchant_order: " + pid + " estado:" + payment.getStatus());
                                // Reusar lógica de éxito/pendiente según status
                                if ("approved".equalsIgnoreCase(payment.getStatus())) {
                                     String alquilerId = payment.getExternalReference();
                                    if (alquilerId == null && payment.getMetadata() != null) {
                                        alquilerId = (String) payment.getMetadata().get("alquilerId");
                                        System.out.println("PAGO EXITOSO");
                                    }
                                } else if ("pending".equalsIgnoreCase(payment.getStatus())) {
                                    System.out.println("PAGO pendiente");
                                }
                            } catch (Exception e) {
                                System.err.println("Error al procesar payment id " + pid + ": " + e.getMessage());
                            }
                        }
                    }
                } else {
                    System.out.println("No se encontraron pagos en merchant_order response");
                }
            } else {
                System.err.println("Error fetching merchant_order: status=" + resp.statusCode());
            }

        } catch (Exception e) {
            System.err.println("Error al obtener merchant_order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Not yet using it
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
        // String calculatedHash = utilServicio.generateHmacSHA256(pagoServicio.getWebhookSecret(), manifest);

        // return calculatedHash.equals(hash);
        return true;
    }

    private void processPayment(Map<String, Object> body) {
        try {
            System.out.println("WEBHOOK BODY: " + body);
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            String resource = (String) body.get("resource");
            String topic = null;
            if (body.containsKey("topic")) topic = String.valueOf(body.get("topic"));
            if (topic == null && body.containsKey("type")) topic = String.valueOf(body.get("type"));
            String paymentId = null;
            if (data != null) {
                paymentId = String.valueOf(data.get("id"));
            } else if (resource != null) {
                // Si el resource es solo un número, lo tomamos como paymentId
                paymentId = resource.contains("/") ? resource.substring(resource.lastIndexOf("/") + 1) : resource;
            }
            // Si el webhook apunta a un merchant_order, debemos llamar al endpoint de merchant_orders
            if (topic != null && topic.toLowerCase().contains("merchant_order")) {
                // resource suele ser la URL completa de merchant_orders
                if (resource != null) {
                    fetchMerchantOrderAndProcessPayments(resource);
                    return;
                }
            }

            // Obtenemos el pago desde Mercado Pago (topic payment)
            if (paymentId == null) {
                System.err.println("No se pudo determinar paymentId desde el webhook");
                return;
            }

            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.valueOf(paymentId));

            if ("approved".equalsIgnoreCase(payment.getStatus())) {
                String alquilerId = payment.getExternalReference();
                if (alquilerId == null && payment.getMetadata() != null) {
                    alquilerId = (String) payment.getMetadata().get("alquilerId");
                }
                System.out.println("PAGO EXITOSO");
                // buscar factura asociada al alquiler
                //facturaServicio.marcarComoPagada(facturaId);
            }
            if ("pending".equalsIgnoreCase(payment.getStatus())) {
                String alquilerId = payment.getExternalReference();
                if (alquilerId == null && payment.getMetadata() != null) {
                    alquilerId = (String) payment.getMetadata().get("alquilerId");
                }
                System.out.println("PAGO pendiente");
                // buscar factura asociada al alquiler
                //facturaServicio.marcarComoPendiente(facturaId);
            }
        } catch (com.mercadopago.exceptions.MPApiException mpEx) {
            // MercadoPago API error: loguear detalles útiles
            System.err.println("MPApiException al consultar MercadoPago: " + mpEx.getMessage());
            try {
                // Algunos SDKs incluyen respuesta HTTP en la excepción
                java.lang.reflect.Method m = mpEx.getClass().getMethod("getApiResponse");
                Object apiResp = m.invoke(mpEx);
                if (apiResp != null) {
                    System.err.println("MP API response object: " + apiResp.getClass().getName());
                    // Intentar extraer campos comunes
                    String[] candidateMethods = {"getStatusCode", "getStatus", "getResponseBody", "getBody", "getResponse"};
                    for (String methodName : candidateMethods) {
                        try {
                            java.lang.reflect.Method mm = apiResp.getClass().getMethod(methodName);
                            Object value = mm.invoke(apiResp);
                            System.err.println("  " + methodName + " => " + String.valueOf(value));
                        } catch (NoSuchMethodException nsme) {
                            // ignore
                        }
                    }
                    // Fallback to toString
                    System.err.println("  toString => " + apiResp.toString());
                }
            } catch (Exception ignore) {
                // no disponible
            }
            mpEx.printStackTrace();
        } catch (Exception e) {
            // Loguear error inesperado
            e.printStackTrace();
        }
    }
}
