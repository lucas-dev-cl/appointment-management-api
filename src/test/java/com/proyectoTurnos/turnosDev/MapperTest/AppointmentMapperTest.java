package com.proyectoTurnos.turnosDev.MapperTest;

import com.proyectoTurnos.turnosDev.DTO.AppointmentDTO;
import com.proyectoTurnos.turnosDev.Entity.Appointment;
import com.proyectoTurnos.turnosDev.Service.Mapper.AppointmentMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentMapperTest {

    private AppointmentMapper appointmentMapper = new AppointmentMapper();

    // Mapper: convierte correctamente una entidad Appointment
    // a su correspondiente AppointmentDTO
    @Test
    void shouldReturnAppointmentDTO(){
        Appointment appointment = new Appointment();
        appointment.setClientName("Lucas");
        appointment.setService("Odontologia");

        AppointmentDTO appointmentDTO = appointmentMapper.toAppointmentDTO(appointment);

        assertEquals("Lucas", appointmentDTO.getClientName());
    }

    // Mapper: convierte correctamente un AppointmentDTO
    // a la entidad Appointment
    @Test
    void shouldReturnAppointment(){
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setClientName("Lucas");
        appointmentDTO.setService("Odontologia");

        Appointment appointment = appointmentMapper.toAppointment(appointmentDTO);

        assertEquals("Lucas", appointment.getClientName());
    }

    // Mapper: convierte una lista de Appointment
    // en una lista de AppointmentDTO
    @Test
    void shouldReturnAppointmentsToDtoListTO(){
        Appointment appointment = new Appointment();
        appointment.setService("Cardiologo");

        List<Appointment> appointmentList = List.of(appointment);

        List<AppointmentDTO> appointmentDTOList = appointmentMapper.appointmentsToDtoListTO(appointmentList);

        assertNotNull(appointmentDTOList);
        assertEquals(1, appointmentDTOList.size());
        assertEquals("Cardiologo", appointmentDTOList.get(0).getService());
    }
}
