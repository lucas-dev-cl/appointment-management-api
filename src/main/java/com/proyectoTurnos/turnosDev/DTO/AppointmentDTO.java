package com.proyectoTurnos.turnosDev.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    @NotBlank(message = "Debe existir un nombre de cliente")
    private String clientName;

    @NotNull
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime dateTime;

    @NotBlank(message = "El campo 'servicio' no puede estar vacio")
    private String service;
}
