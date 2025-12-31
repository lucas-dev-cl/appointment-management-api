package com.proyectoTurnos.turnosDev.Repository;

import com.proyectoTurnos.turnosDev.Entity.Appointment;
import com.proyectoTurnos.turnosDev.Entity.AppointmentStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AppointmentCustomRepository {
    List<Appointment> search(
            String clientName,
            String service,
            AppointmentStatus status
    );

}
