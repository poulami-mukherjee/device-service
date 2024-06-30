package com.example.deviceservice.controller;

import com.example.deviceservice.dto.DeviceDto;
import com.example.deviceservice.model.Device;
import com.example.deviceservice.service.DeviceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devices")
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * Handles the POST request to add a new device.
     *
     * @param deviceDto The data transfer object containing device information.
     * @return ResponseEntity containing the created Device.
     *         Returns a 201 Created response with the device data if the device is successfully created.
     *         Returns a 400 Bad Request response if the request is invalid.
     *         In case of an internal server error, logs the error and returns a 500 Internal Server Error response.
     */
    @PostMapping
    public ResponseEntity<Device> addDevice(@Valid @RequestBody DeviceDto deviceDto) {
        try {
            Device device = deviceService.createDevice(deviceDto);
            log.info("Device: {} of brand: {} is created", deviceDto.name(), deviceDto.brand());
            return ResponseEntity.status(HttpStatus.CREATED).body(device);
        } catch (Exception ex) {
            log.error("Internal server error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}