package com.schedul.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.lang.InterruptedException;
import jakarta.mail.MessagingException;

import java.util.Map;


/*
fixedRate: intervalos fijos desde el inicio
fixedDelay: intervalos fijos desde el final
initialDelay: retrasa primera ejecucion
cron: ejecuta segun una expresion CRON
zone: especifica la zona horaria de una expresion CRON
*/

@Component
public class ScheduleNewYear {
	
	private final ScheduleEmail emailService;

	public ScheduleNewYear(ScheduleEmail emailService) {
		this.emailService = emailService;
	}

	//@Scheduled(fixedDelay = 5000, initialDelay = 3000) 
	//5 seg desde que termina, tarda 3 seg en empezar
	@Scheduled(cron = "0 0 0 31 12 *", zone = "America/Argentina/Buenos_Aires") //Año nuevo
	//*seg *min *hr *diaMes *mes *diaSemana --> reemplaza * con 0 cuando se desplace a la izquierda
	// 0 0 15 * * 1,3,5 --> a las 3pm los lunes, miercoles y viernes
	/*
	  @yearly --> 0 0 0 1 1 *
	  @monthly --> 0 0 0 1 * *
	  @weekly --> 0 0 0 * * 0
	  @daily --> 0 0 0 * * *
	  @hourly --> 0 0 * * * *
	*/
	public void scheduleMessage() {

        
    	try {
	        	emailService.sendHtmlEmail(
                "receiver@example.com",
                "Happy New Year!",
                "yearend-email",
                Map.of(
                    "buttonUrl", "https://ingenieria.uncuyo.edu.ar",
                    "buttonText", "Aproveche y vaya a la Facultad"
                	)
        		);
    	
			} 
		catch (MessagingException e) {
			    e.printStackTrace(); // or log the error
			}
	    //System.out.println("Hola Mundo!!");
	    /*try {
	        Thread.sleep(7000);
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	        System.err.println("Task interrupted: " + e.getMessage());
	    }*/
	}
}