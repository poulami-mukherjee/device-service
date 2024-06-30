package com.example.deviceservice.exception;

/**
 * Custom exception for service layer errors.
 */
public class DeviceServiceException extends RuntimeException {
    public DeviceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
