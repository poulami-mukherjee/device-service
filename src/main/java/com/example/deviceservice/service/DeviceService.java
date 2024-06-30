package com.example.deviceservice.service;

import com.example.deviceservice.dto.DeviceDto;
import com.example.deviceservice.exception.DeviceServiceException;
import com.example.deviceservice.model.Device;
import com.example.deviceservice.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing {@link Device} entities.
 * Provides methods for creating, retrieving, updating, and deleting devices.
 */
@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * Creates a new device.
     *
     * @param deviceDto the data transfer object containing the device details
     * @return the created device
     * @throws DeviceServiceException if an error occurs while creating the device
     */

    @Transactional
    public Device createDevice(DeviceDto deviceDto) {
        try {
            Device device = Device.builder()
                    .name(deviceDto.name())
                    .brand(deviceDto.brand())
                    .creationTime(LocalDateTime.now())
                    .build();
            return deviceRepository.save(device);
        } catch (Exception e) {
            throw new DeviceServiceException("Error creating device", e);
        }
    }
}
