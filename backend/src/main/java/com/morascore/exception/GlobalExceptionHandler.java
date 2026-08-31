package com.morascore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Validación de @Valid — errores de campos en request body (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "Bad Request");
        cuerpo.put("messages", errores);
        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }

    // 2. No encontrado (404)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(NoSuchElementException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", HttpStatus.NOT_FOUND.value());
        cuerpo.put("error", "Not Found");
        cuerpo.put("message", ex.getMessage());
        return new ResponseEntity<>(cuerpo, HttpStatus.NOT_FOUND);
    }

    // 3. Petición inválida (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarPeticionInvalida(IllegalArgumentException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "Bad Request");
        cuerpo.put("message", ex.getMessage());
        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }

    // 4. Estado ilegal — cubre casos como respuesta nula del modelo ML (500)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> manejarEstadoIlegal(IllegalStateException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        cuerpo.put("error", "Internal Server Error");
        cuerpo.put("message", ex.getMessage());
        return new ResponseEntity<>(cuerpo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 5. Error de conexión al servicio ML / FastAPI (503)
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> manejarErrorConexionML(ResourceAccessException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        cuerpo.put("error", "Service Unavailable");
        cuerpo.put("message", "No se pudo conectar al servicio de predicción ML. Verifica que FastAPI esté corriendo.");
        return new ResponseEntity<>(cuerpo, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // 6. Otros errores HTTP del cliente REST (502)
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, Object>> manejarErrorRestClient(RestClientException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", HttpStatus.BAD_GATEWAY.value());
        cuerpo.put("error", "Bad Gateway");
        cuerpo.put("message", "El servicio ML retornó una respuesta inesperada: " + ex.getMessage());
        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_GATEWAY);
    }

    // 7. Error genérico — expone el mensaje para facilitar diagnóstico (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(Exception ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        cuerpo.put("error", "Internal Server Error");
        log.error("Error no manejado capturado: ", ex);
        cuerpo.put("message", "Ha ocurrido un error interno. Contacta al soporte.");
        return new ResponseEntity<>(cuerpo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
