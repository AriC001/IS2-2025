package com.sport.proyecto.servicios;


import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.exceptions.MPApiException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.util.Collections;


@Service
public class PagoServicio {
    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.payment-validation-secret}")
    private String webhookToken;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public String createPreference() throws Exception {
        PreferenceItemRequest item =
                PreferenceItemRequest.builder()
                        .title("Pago Cuota")
                        .quantity(1)
                        .unitPrice(BigDecimal.valueOf(100f))
                        .id("message")
                        .build();

        PreferenceBackUrlsRequest backUrls =
                PreferenceBackUrlsRequest.builder()
                        .success("https://subarboreous-unprovokingly-elmira.ngrok-free.dev/payment/success")
                        .failure("https://subarboreous-unprovokingly-elmira.ngrok-free.dev/payment/failure")
                        .pending("https://subarboreous-unprovokingly-elmira.ngrok-free.dev/payment/pending")
                        .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(Collections.singletonList(item))
                .backUrls(backUrls)
                .autoReturn("approved")
                .build();

        PreferenceClient client = new PreferenceClient();
        try {
            Preference preference = client.create(request);
            return preference.getInitPoint();
        } catch (MPApiException e) {
            System.out.println("API Error: " + e.getApiResponse().getContent());
            throw e;
        }
    }

    public String getWebhookSecret(){
        return webhookToken;
    }
}
