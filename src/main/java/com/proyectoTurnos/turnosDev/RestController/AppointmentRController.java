package com.proyectoTurnos.turnosDev.RestController;

import com.proyectoTurnos.turnosDev.DTO.AppointmentDTO;
import com.proyectoTurnos.turnosDev.Entity.AppointmentStatus;
import com.proyectoTurnos.turnosDev.Service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app")
public class AppointmentRController {

    private AppointmentService appointmentService;

    public AppointmentRController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @PostMapping("/createAppointment")
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.createAppointment(appointmentDTO));
    }

    @GetMapping("/appointment")
    public ResponseEntity<List<AppointmentDTO>> getAppointment(@RequestParam(required = false) String clientName, @RequestParam(required = false) String service, @RequestParam(required = false) AppointmentStatus status){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appointmentService.getAppointment(clientName, service, status));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String id){
        appointmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/completed")
    public ResponseEntity<Void> completed(@PathVariable String id){
        appointmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}