package com.example.deviceservice.exception;

/**
 * Custom exception thrown when a device is not found.
 */
public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Long id) {
        super("Device not found with id: " + id);
    }
}
