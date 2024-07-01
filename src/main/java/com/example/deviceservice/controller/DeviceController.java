package com.example.deviceservice.controller;

import com.example.deviceservice.dto.DeviceDto;
import com.example.deviceservice.model.Device;
import com.example.deviceservice.service.DeviceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.deviceservice.exception.DeviceNotFoundException;
import com.example.deviceservice.exception.DeviceServiceException;
import java.util.List;

@RestController
@RequestMapping("/devices")
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * Creates a new device.
     *
     * @param deviceDto the data transfer object containing the device details
     * @return the created device
     * @throws DeviceServiceException if an error occurs while creating the device
     */
    @PostMapping("/create")
    public ResponseEntity<Device> addDevice(@Valid @RequestBody DeviceDto deviceDto) {
        Device device = deviceService.createDevice(deviceDto);
        log.info("Device: {} of Brand: {} created", deviceDto.name(), deviceDto.brand());
        return ResponseEntity.status(201).body(device);
    }

    /**
     * Retrieves a device by its ID.
     *
     * @param id the ID of the device to retrieve
     * @return the device with the specified ID
     * @throws DeviceNotFoundException if the device with the specified ID is not found
     * @throws DeviceServiceException if an error occurs while retrieving the device
     */
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        Device device = deviceService.findDeviceById(id);
        return ResponseEntity.ok(device);
    }

    /**
     * Retrieves all devices.
     *
     * @return a list of all devices
     * @throws DeviceServiceException if an error occurs while retrieving devices
     */
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.findAllDevices();
        return ResponseEntity.ok(devices);
    }

    /**
     * Searches for devices by their brand.
     *
     * @param brand the brand of the devices to search for
     * @return a list of devices with the specified brand
     * @throws DeviceServiceException if an error occurs while finding devices by brand
     */
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Device>> searchDevicesByBrand(@PathVariable String brand) {
        List<Device> devices = deviceService.findDevicesByBrand(brand);
        return ResponseEntity.ok(devices);
    }

    /**
     * Updates an existing device.
     *
     * @param id the ID of the device to update
     * @param deviceDto the data transfer object containing the updated device details
     * @return the updated device
     * @throws DeviceNotFoundException if the device with the specified ID is not found
     * @throws DeviceServiceException if an error occurs while updating the device
     */
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @Valid @RequestBody DeviceDto deviceDto) {
        Device updatedDevice = deviceService.updateDevice(id, deviceDto);
        return ResponseEntity.ok(updatedDevice);
    }

    /**
     * Partially updates an existing device.
     *
     * @param id the ID of the device to update
     * @param deviceDto the data transfer object containing the updated device details
     * @return the updated device
     * @throws DeviceNotFoundException if the device with the specified ID is not found
     * @throws DeviceServiceException if an error occurs while updating the device
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Device> partialUpdateDevice(@PathVariable Long id, @RequestBody DeviceDto deviceDto) {
        Device existingDevice = deviceService.findDeviceById(id);
        if (deviceDto.name() != null) {
            existingDevice.setName(deviceDto.name());
        }
        if (deviceDto.brand() != null) {
            existingDevice.setBrand(deviceDto.brand());
        }
        Device updatedDevice = deviceService.updateDevice(id, deviceDto);
        return ResponseEntity.ok(updatedDevice);
    }

    /**
     * Deletes a device by its ID.
     *
     * @param id the ID of the device to delete
     * @return a response entity with status code 204 (No Content)
     * @throws DeviceNotFoundException if the device with the specified ID is not found
     * @throws DeviceServiceException if an error occurs while deleting the device
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
