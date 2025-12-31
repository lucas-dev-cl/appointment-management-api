package com.proyectoTurnos.turnosDev.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;

// El wrapper que en la respuesta aparecera como un objeto "error"
@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private ApiError apiError;

}
