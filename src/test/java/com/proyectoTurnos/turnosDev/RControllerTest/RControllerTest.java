package com.proyectoTurnos.turnosDev.RControllerTest;

import com.proyectoTurnos.turnosDev.DTO.AppointmentDTO;
import com.proyectoTurnos.turnosDev.Exception.AppointmentNotFoundException;
import com.proyectoTurnos.turnosDev.RestController.AppointmentRController;
import com.proyectoTurnos.turnosDev.Service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;

// Test unitario de un endpoint
@WebMvcTest(AppointmentRController.class)
public class RControllerTest {

    @Autowired
    private ObjectMapper objectMapper; // Clase de Jackson que me permite serializar y deserializar un objeto

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    // Happy path: se crea un turno correctamente,
    // el controller delega en el service y devuelve 201 Created
    // junto con el AppointmentDTO en el body
    @Test
    void shouldCreateAppointment() throws Exception{
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setClientName("Lucas");
        appointmentDTO.setService("Odontologia");
        appointmentDTO.setDateTime(LocalDateTime.now().plusDays(1));

        when(appointmentService.createAppointment(any()))
                .thenReturn(appointmentDTO);

        mockMvc.perform(post("/app/createAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentDTO)))
                .andExpect(status().isCreated()) // Verificamos codigo http
                .andExpect(jsonPath("$.clientName").value("Lucas")); // Validamos body
    }

    // Testeo que la respuesta sea la del RestControllerAdvice
    // No usamos when porque nunca llega a usar el servicio
    @Test
    void shouldReturnBadRequest_whenClientNameIsBlank() throws Exception {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setClientName("");
        appointmentDTO.setService("Odontologia");
        appointmentDTO.setDateTime(LocalDateTime.now().plusDays(1));


        mockMvc.perform(post("/app/createAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentDTO))) // Serializamos el objeto
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.apiError.message").exists()); // verificamos que el campo exista
    }

    // Happy path: el endpoint cancela el turno y delega en el service
    // Retorna 204 y no lanza excepciones
    @Test
    void shouldChangeStatusAppointment() throws  Exception{
        // doNothing se usa para metodos void, para dejar explicito el comportamiento
        doNothing().when(appointmentService).cancel(anyString());

        mockMvc.perform(patch("/app/1/cancel"))
                .andExpect(status().isNoContent());

        verify(appointmentService).cancel(anyString());
    }

    // Error path: cuando el turno no existe,
    // el service lanza AppointmentNotFoundException
    // y el ControllerAdvice la traduce a HTTP 404
    @Test
    void shouldReturnNotFound_whenAppointmentDoesNotExist() throws Exception {

        // Lanzamos la excepcion cuando usemos ese metodo del servicio
        // Se usa doThrow cuando simulamos un metodo que no retorna nada, si retorna algo entonces when(...).thenReturn(...)
        doThrow(new AppointmentNotFoundException("Appointment Not Found"))
                .when(appointmentService)
                .cancel(anyString());

        mockMvc.perform(patch("/app/1/cancel"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.apiError.message").exists());
    }
}
