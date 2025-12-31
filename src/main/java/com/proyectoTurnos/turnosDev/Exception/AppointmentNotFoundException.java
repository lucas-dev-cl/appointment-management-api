package com.proyectoTurnos.turnosDev.Exception;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String id) {
        super("No se encontro el turno con el id: " + id);
    }
}
