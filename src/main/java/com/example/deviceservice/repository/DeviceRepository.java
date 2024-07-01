package com.example.deviceservice.repository;

import com.example.deviceservice.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Device} entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    /**
     * Finds devices by their brand.
     *
     * @param brand the brand of the devices to find
     * @return a list of devices with the specified brand
     */
    List<Device> findByBrand(String brand);
}
