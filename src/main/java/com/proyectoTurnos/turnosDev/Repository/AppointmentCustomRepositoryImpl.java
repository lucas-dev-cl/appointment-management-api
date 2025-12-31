package com.proyectoTurnos.turnosDev.Repository;

import com.proyectoTurnos.turnosDev.Entity.Appointment;
import com.proyectoTurnos.turnosDev.Entity.AppointmentStatus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class AppointmentCustomRepositoryImpl implements AppointmentCustomRepository{

    private final MongoTemplate mongoTemplate;

    public AppointmentCustomRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Appointment> search(String clientName, String service, AppointmentStatus status) {
        Query query = new Query();

        if(clientName != null && !clientName.isEmpty()){
            query.addCriteria(
                    Criteria.where("clientName")
                            .regex(Pattern.compile(Pattern.quote(clientName), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
            );
        }

        if(service != null && !service.isEmpty()){
            query.addCriteria(
                    Criteria.where("service").is(service)
                            .regex(Pattern.compile(Pattern.quote(service), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
            );
        }

        if(status != null){
            query.addCriteria(
                    Criteria.where("status")
                            .regex(Pattern.compile(Pattern.quote(status.name()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))

            );
        }

        return mongoTemplate.find(query, Appointment.class);
    }
}
