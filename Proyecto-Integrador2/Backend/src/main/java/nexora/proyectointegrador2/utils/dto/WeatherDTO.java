package nexora.proyectointegrador2.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class WeatherDTO {
    private Main main;
    private List<Weather> weather;
    private Wind wind;
    private String name;

    @Data
    public static class Main {
        private double temp;
        private double humidity;
    }

    @Data
    public static class Weather {
        private String description;
        private String icon;
    }

    @Data
    public static class Wind {
        private double speed;
    }
}