package com.proyectoTurnos.turnosDev.Exception;

public class AppointmentAlreadyCancelledException extends RuntimeException {
    public AppointmentAlreadyCancelledException(String message) {
        super(message);
    }
}
