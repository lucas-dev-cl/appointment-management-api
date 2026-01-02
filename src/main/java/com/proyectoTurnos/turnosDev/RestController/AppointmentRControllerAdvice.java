package com.proyectoTurnos.turnosDev.RestController;

import com.proyectoTurnos.turnosDev.Exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AppointmentRControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            HttpServletRequest httpServletRequest,
            MethodArgumentNotValidException ex
    ){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ApiError apiError = new ApiError(
                400,
                "Validation error",
                message,
                httpServletRequest.getContextPath(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(
                new ApiErrorResponse(apiError)
        );
    }

    @ExceptionHandler(AppointmentInPastException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            HttpServletRequest httpServletRequest,
            AppointmentInPastException ex
    ){
        ApiError apiError = new ApiError(
                400,
                "Validation error",
                ex.getMessage(),
                httpServletRequest.getContextPath(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(
                new ApiErrorResponse(apiError)
        );
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            AppointmentNotFoundException ex,
            HttpServletRequest httpServletRequest
    ){
        ApiError apiError = new ApiError(
                404,
                "Not Found Error",
                ex.getMessage(),
                httpServletRequest.getContextPath(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(apiError)
        );
    }

    @ExceptionHandler(InvalidAppointmentStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleStatusError(
            InvalidAppointmentStatusException ex,
            HttpServletRequest httpServletRequest
    ){
        ApiError apiError = new ApiError(
                400,
                "Status Error",
                ex.getMessage(),
                httpServletRequest.getContextPath(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(apiError));
    }

}
