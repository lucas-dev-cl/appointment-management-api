package com.proyectoTurnos.turnosDev.Repository;

import com.proyectoTurnos.turnosDev.Entity.Appointment;
import com.proyectoTurnos.turnosDev.Entity.AppointmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    Optional<Appointment> findById(Long id);

    List<Appointment> findAll();

    List<Appointment> findByClientNameAndServiceAndStatus(String clienName, String service, AppointmentStatus status);

    List<Appointment> findByClientName(String clientName);

    List<Appointment> findByService(String service);
}
