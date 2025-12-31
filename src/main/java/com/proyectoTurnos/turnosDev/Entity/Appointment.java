package com.proyectoTurnos.turnosDev.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "turnos")
public class Appointment {

    @Id
    private String id;
    private String clientName;
    private LocalDateTime dateTime;
    private String service;
    private AppointmentStatus status;

    public Appointment(String clientName, LocalDateTime dateTime, String service){
        this.clientName = clientName;
        this.dateTime = dateTime;
        this.service = service;
    }
}
