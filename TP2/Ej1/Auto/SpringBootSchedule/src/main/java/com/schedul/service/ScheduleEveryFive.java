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
public class ScheduleEveryFive {
	
	private final ScheduleEmail emailService;

	public ScheduleEveryFive(ScheduleEmail emailService) {
		this.emailService = emailService;
	}

	//@Scheduled(fixedDelay = 5000, initialDelay = 3000) 
	//5 seg desde que termina, tarda 3 seg en empezar
	@Scheduled(cron = "0 0 15 5 * *", zone = "America/Argentina/Buenos_Aires")
	//*seg *min *hr *diaMes *mes *diaSemana --> reemplaza * con 0 cuando se desplace a la izquierda
	// 0 0 15 5 * * --> a las 3pm el dia 5 de cada mes
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
	                "Monthly Reminder",
	                "monthly-email",
	                Map.of(
	                    "buttonUrl", "https://ingenieria.uncuyo.edu.ar",
	                    "buttonText", "Vaya a la Facultad"
	                )
			        );
			    } 
			catch (MessagingException e) {
			        e.printStackTrace(); // or log the error
			    }
		}

}