package com.proyectoTurnos.turnosDev.Service;

import com.proyectoTurnos.turnosDev.DTO.AppointmentDTO;
import com.proyectoTurnos.turnosDev.Entity.Appointment;
import com.proyectoTurnos.turnosDev.Entity.AppointmentStatus;
import com.proyectoTurnos.turnosDev.Exception.AppointmentInPastException;
import com.proyectoTurnos.turnosDev.Exception.AppointmentNotFoundException;
import com.proyectoTurnos.turnosDev.Exception.InvalidAppointmentStatusException;
import com.proyectoTurnos.turnosDev.Repository.AppointmentCustomRepository;
import com.proyectoTurnos.turnosDev.Repository.AppointmentRepository;
import com.proyectoTurnos.turnosDev.Service.Mapper.AppointmentMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentCustomRepository appointmentCustomRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, AppointmentCustomRepository appointmentCustomRepository){
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.appointmentCustomRepository = appointmentCustomRepository;
    }

    // Busca un turno por id.
    // Si no existe, lanza AppointmentNotFoundException.
    // Si existe, lo mapea a DTO y lo devuelve
    public AppointmentDTO getAppointmentById(String id){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        return appointmentMapper.toAppointmentDTO(appointment);
    }

    // Crea un nuevo turno.
    // Valida que la fecha sea futura, de lo contrario lanza excepción.
    // Si es válida, mapea el DTO a entidad, setea el estado inicial
    // como SCHEDULED, persiste y devuelve el DTO creado
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO){
        if(appointmentDTO.getDateTime().isBefore(LocalDateTime.now())){
            throw new AppointmentInPastException("La fecha debe ser futura");
        }

        Appointment appointment = appointmentMapper.toAppointment(appointmentDTO);

        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointmentRepository.save(appointment);

        return appointmentMapper.toAppointmentDTO(appointment);
    }

    // Cancela un turno existente.
    // Si el turno no existe, lanza AppointmentNotFoundException.
    // Si existe, cambia su estado a CANCELLED y lo persiste
    public void cancel(String id){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        if(appointment.getStatus().equals(AppointmentStatus.CANCELLED)){
            throw new InvalidAppointmentStatusException("El turno ya está cancelado");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    // Marca un turno como completado.
    // Si el turno no existe, lanza AppointmentNotFoundException.
    // Si existe, cambia su estado a COMPLETED y lo persiste
    public void completed(String id){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        if(appointment.getStatus().equals(AppointmentStatus.COMPLETED)){
            throw new InvalidAppointmentStatusException("El turno ya está completado");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    // Busca turnos aplicando filtros opcionales (cliente, servicio y estado)
    // usando el repositorio custom y devuelve la lista mapeada a DTO
    public List<AppointmentDTO> getAppointment(String clientName, String service, AppointmentStatus status){
        return appointmentMapper.appointmentsToDtoListTO(appointmentCustomRepository.search(clientName, service, status));
    }

}
