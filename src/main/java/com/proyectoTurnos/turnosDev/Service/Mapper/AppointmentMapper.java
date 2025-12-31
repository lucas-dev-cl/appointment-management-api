package com.proyectoTurnos.turnosDev.Service.Mapper;

import com.proyectoTurnos.turnosDev.DTO.AppointmentDTO;
import com.proyectoTurnos.turnosDev.Entity.Appointment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentMapper {

    public AppointmentDTO toAppointmentDTO(Appointment appointment){
        return new AppointmentDTO(
                appointment.getClientName(),
                appointment.getDateTime(),
                appointment.getService()
        );
    }

    public Appointment toAppointment(AppointmentDTO appointmentDTO){
        return new Appointment(
                appointmentDTO.getClientName(),
                appointmentDTO.getDateTime(),
                appointmentDTO.getService()
        );
    }

    public List<AppointmentDTO> appointmentsToDtoListTO(List<Appointment> appointments){
        return appointments.stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getClientName(),
                        appointment.getDateTime(),
                        appointment.getService()
                ))
                .toList();
    }
}
