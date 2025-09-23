package com.sport.proyecto.config;
import com.mercadopago.MercadoPagoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfigApp {

    public MercadoPagoConfigApp(@Value("${mercadopago.access-token}") String accessToken) {
        MercadoPagoConfig.setAccessToken(accessToken);
    }
}
