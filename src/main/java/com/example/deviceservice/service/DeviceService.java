package com.example.deviceservice.service;

import com.example.deviceservice.dto.DeviceDto;
import com.example.deviceservice.exception.DeviceNotFoundException;
import com.example.deviceservice.exception.DeviceServiceException;
import com.example.deviceservice.model.Device;
import com.example.deviceservice.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    /**
     * Finds a device by its ID.
     *
     * @param id the ID of the device to find
     * @return the found device
     * @throws DeviceNotFoundException if the device with the specified ID is not found
     * @throws DeviceServiceException if an error occurs while retrieving the device
     */
    public Device findDeviceById(Long id) {
        try {
            return deviceRepository.findById(id)
                    .orElseThrow(() -> new DeviceNotFoundException(id));
        } catch (DeviceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DeviceServiceException("Error retrieving device by ID", e);
        }
    }

    /**
     * Retrieves all devices.
     *
     * @return a list of all devices
     * @throws DeviceServiceException if an error occurs while retrieving devices
     */
    public List<Device> findAllDevices() {
        try {
            return deviceRepository.findAll();
        } catch (Exception e) {
            throw new DeviceServiceException("Error retrieving devices", e);
        }
    }

    /**
     * Finds devices by their brand.
     *
     * @param brand the brand of the devices to find
     * @return a list of devices with the specified brand
     * @throws DeviceServiceException if an error occurs while finding devices by brand
     */
    public List<Device> findDevicesByBrand(String brand) {
        try {
            return deviceRepository.findByBrand(brand);
        } catch (Exception e) {
            throw new DeviceServiceException("Error finding devices by brand", e);
        }
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
    @Transactional
    public Device updateDevice(Long id, DeviceDto deviceDto) {
        try {
            Device device = findDeviceById(id);
            device.setName(deviceDto.name());
            device.setBrand(deviceDto.brand());
            return deviceRepository.save(device);
        } catch (Exception e) {
            throw new DeviceServiceException("Error updating device", e);
        }
    }

    /**
     * Deletes a device by its ID.
     *
     * @param id the ID of the device to delete
     * @throws DeviceNotFoundException if the device with the specified ID is not found
     * @throws DeviceServiceException if an error occurs while deleting the device
     */
    @Transactional
    public void deleteDevice(Long id) {
        try {
            if (!deviceRepository.existsById(id)) {
                throw new DeviceNotFoundException(id);
            }
            deviceRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeviceServiceException("Error deleting device", e);
        }
    }
}
