package com.nexora.proyectointegrador2.front_cliente.business.logic.service;


import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.exceptions.MPApiException;
//import com.sport.proyecto.entidades.Factura;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.resources.preference.Preference;
import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.AlquilerDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.AlquilerDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;

import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


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

    public String createPreference(VehiculoDTO vehiculoDTO, AlquilerDTO alquilerDTO) throws Exception {
        //Factura factura = facturaServicio.buscarFactura(facturaId);

        List<PreferenceItemRequest> items = List.of(
                PreferenceItemRequest.builder()
                        .title("Pago Alquiler Auto " + vehiculoDTO.getCaracteristicaVehiculo().getMarca() + " " + vehiculoDTO.getCaracteristicaVehiculo().getModelo())
                        .quantity(1)
                        .unitPrice(BigDecimal.valueOf(vehiculoDTO.getCaracteristicaVehiculo().getCostoVehiculo().getCosto()))
                        .id(alquilerDTO.getId().toString()) // acá podés poner el id real de la cuota
                        .build()
        );

        PreferenceBackUrlsRequest backUrls =
                PreferenceBackUrlsRequest.builder()
                        .success("https://p79t602v-8082.brs.devtunnels.ms/payment/success")
                        .failure("https://subarboreous-unprovokingly-elmira.ngrok-free.dev/payment/failure")
                        .pending("https://subarboreous-unprovokingly-elmira.ngrok-free.dev/payment/pending")
                        .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .metadata(Collections.singletonMap("alquilerId", alquilerDTO.getId().toString()))
                .backUrls(backUrls)
                .autoReturn("approved")
                .externalReference(alquilerDTO.getId().toString())
                .notificationUrl("https://subarboreous-unprovokingly-elmira.ngrok-free.dev/webhook")
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
