package com.proyectoTurnos.turnosDev.Exception;

public class AppointmentInPastException extends RuntimeException {

    public AppointmentInPastException(String message) {
        super(message);
    }

}
