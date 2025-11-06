package com.example.taller.service.observer;

import java.util.ArrayList;
import java.util.List;

public class NotificadorObserver {

    private static NotificadorObserver instance;
    private final List<EventListener> listeners = new ArrayList<>();

    private NotificadorObserver() {}

    public static synchronized NotificadorObserver getInstance() {
        if (instance == null) {
            instance = new NotificadorObserver();
        }
        return instance;
    }

    public void suscribir(EventListener listener) {
        listeners.add(listener);
    }

    public void notificar(String evento) {
        for (EventListener l : listeners) {
            l.update(evento);
        }
    }
}
