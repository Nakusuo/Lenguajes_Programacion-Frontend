package com.minerva.infrastructure.rest.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author L
 */
@ControllerAdvice
public class GlobalExceptionHandler {
/*
    @Autowired
    private ExceptionLogService exceptionLogService;

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        logException(ex);

        model.addAttribute("error", "Error inesperado");
        model.addAttribute("message", ex.getMessage());

        return "error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        logException(ex);

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos");

        return ResponseEntity
                .badRequest()
                .body(new MessageDto(errorMessage));
    }

    // Registra la exepcion
    private void logException(Exception ex) {
        exceptionLogService.create(
                ex.getClass().getName(),
                ex.getMessage(),
                getStackTraceAsString(ex),
                ex.getCause() != null ? ex.getCause().toString() : null
        );
    }

    // Convierte el stack trace a String
    private String getStackTraceAsString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }*/
}
