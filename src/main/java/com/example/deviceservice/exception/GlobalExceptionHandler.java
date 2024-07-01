package com.example.deviceservice.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling exceptions and returning appropriate HTTP responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles DeviceNotFoundException and returns a 404 response.
     *
     * @param ex the exception
     * @return the response entity with the error message and HTTP status 404
     */
    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<String> handleDeviceNotFoundException(DeviceNotFoundException ex) {
        log.error("Device Not Found Exception");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles DeviceServiceException and returns a 500 response.
     *
     * @param ex the exception
     * @return the response entity with the error message and HTTP status 500
     */
    @ExceptionHandler(DeviceServiceException.class)
    public ResponseEntity<String> handleDeviceServiceException(DeviceServiceException ex) {
        log.error("Internal Error - Device Service Exception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    /**
     * Handles generic exceptions and returns a 500 response.
     *
     * @param ex the exception
     * @return the response entity with the error message and HTTP status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("Internal Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
