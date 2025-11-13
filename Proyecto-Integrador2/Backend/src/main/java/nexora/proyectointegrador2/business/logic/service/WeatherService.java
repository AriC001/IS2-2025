package nexora.proyectointegrador2.business.logic.service;

import nexora.proyectointegrador2.utils.dto.WeatherDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    
    @Value("${weather.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    
    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }
    
    public WeatherDTO getWeatherForMendoza() {
        String url = String.format("%s?q=Mendoza,AR&units=metric&lang=es&appid=%s", 
                                   WEATHER_URL, apiKey);
        
        System.out.println("=== WEATHER SERVICE DEBUG ===");
        System.out.println("API Key: " + (apiKey != null ? "EXISTS" : "NULL"));
        System.out.println("URL: " + url);
        
        try {
            WeatherDTO result = restTemplate.getForObject(url, WeatherDTO.class);
            System.out.println("Response: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("ERROR calling weather API: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}