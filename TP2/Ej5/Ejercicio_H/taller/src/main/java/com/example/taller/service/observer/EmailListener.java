package com.example.taller.service.observer;

public class EmailListener implements EventListener {
    @Override
    public void update(String evento) {
        System.out.println("📧 Enviando notificación por correo: " + evento);
    }
}
