package com.proyectoTurnos.turnosDev.ServiceTest;

import com.proyectoTurnos.turnosDev.DTO.AppointmentDTO;
import com.proyectoTurnos.turnosDev.Entity.Appointment;
import com.proyectoTurnos.turnosDev.Entity.AppointmentStatus;
import com.proyectoTurnos.turnosDev.Exception.AppointmentInPastException;
import com.proyectoTurnos.turnosDev.Repository.AppointmentRepository;
import com.proyectoTurnos.turnosDev.Service.AppointmentService;
import com.proyectoTurnos.turnosDev.Service.Mapper.AppointmentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void createAppointmentServiceSuccessfully(){
        // Estableciendo los parametros para el when
        // Seteamos el localdatetime porque es lo primero que verifica el servicio
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDateTime(LocalDateTime.now().plusDays(1));

        Appointment appointment = new Appointment();

        // Logica del servicio
        when(appointmentMapper.toAppointment(appointmentDTO))
                .thenReturn(appointment);

        when(appointmentRepository.save(appointment))
                .thenReturn(appointment);

        when(appointmentMapper.toAppointmentDTO(appointment))
                .thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.createAppointment(appointmentDTO);

        assertNotNull(result);
        assertEquals(AppointmentStatus.SCHEDULED, appointment.getStatus());

        // Verificamos que la llamada al metodo se realice
        verify(appointmentRepository).save(appointment);
    }

    // Error path (service): si la fecha del turno está en el pasado,
    // el servicio lanza AppointmentInPastException
    // y no debe persistirse nada en la base de datos
    // No usamos "when" porque el servicio falla antes de llegar a las dependecias
    @Test
    void shouldFailWhenDateIsInPast(){
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDateTime(LocalDateTime.now().minusDays(1));

        assertThrows(
                AppointmentInPastException.class,
                () -> appointmentService.createAppointment(appointmentDTO)
        );

        // Confirmá que el método save NO fue llamado ni una sola vez, con ningún objeto
        verify(appointmentRepository, never()).save(any());
    }

    // Happy path (service): cuando el turno existe,
    // el service lo obtiene desde el repositorio,
    // lo mapea a DTO y lo devuelve correctamente
    @Test
    void getAppointmentById_shouldReturnDTO_whenExists(){
        Appointment appointment = new Appointment();
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        when(appointmentRepository.findById(anyString()))
                .thenReturn(Optional.of(appointment));

        when(appointmentMapper.toAppointmentDTO(appointment))
                .thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.getAppointmentById(anyString());

        assertNotNull(result);
        // Verificamos que devuelva un dto, nos importa el tipado que retorna
        assertEquals(result, appointmentDTO);

        verify(appointmentRepository).findById(anyString());
        verify(appointmentMapper).toAppointmentDTO(appointment);
    }

    // Error path (service): cuando el turno no existe,
    // el repositorio devuelve Optional.empty()
    // y el service lanza una excepción,
    // sin intentar mapear a DTO
    @Test
    void getAppointmentById_shouldThrowException_whenNotExists(){
        when(appointmentRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> appointmentService.getAppointmentById(anyString())
        );

        verify(appointmentRepository).findById(anyString());
        verify(appointmentMapper, never()).toAppointmentDTO(any());
    }

    // Happy path (service): el service obtiene todos los turnos,
    // los mapea a una lista de DTOs
    // y devuelve la colección correctamente
    @Test
    void getAllAppointments_shouldReturnListDTO(){
        Appointment appointment = new Appointment();
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        when(appointmentMapper.appointmentsToDtoListTO(List.of(appointment)))
                .thenReturn(List.of(appointmentDTO));
        when(appointmentRepository.findAll())
                .thenReturn(List.of(appointment));

        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(appointmentDTO, result.get(0));
    }

    // Happy path (service): cuando el turno existe,
    // el service cambia su estado de SCHEDULED a CANCELLED
    @Test
    void shouldCancelAppointment(){
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(anyString()))
                .thenReturn(Optional.of(appointment));

        appointmentService.cancel("123");

        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
    }
}
