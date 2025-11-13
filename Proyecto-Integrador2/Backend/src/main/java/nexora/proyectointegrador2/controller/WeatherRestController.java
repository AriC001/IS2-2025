package nexora.proyectointegrador2.controller;
import nexora.proyectointegrador2.utils.dto.WeatherDTO;
import nexora.proyectointegrador2.business.logic.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherRestController {
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/mendoza")
    public ResponseEntity<WeatherDTO> getWeather() {
        WeatherDTO weather = weatherService.getWeatherForMendoza();
        if (weather != null) {
            return ResponseEntity.ok(weather);
        }
        return ResponseEntity.status(500).build();
    }
}