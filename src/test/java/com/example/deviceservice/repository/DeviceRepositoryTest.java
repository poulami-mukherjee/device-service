package com.example.deviceservice.repository;

import com.example.deviceservice.model.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

    private Device device1;
    private Device device2;

    @BeforeEach
    public void setUp() {
        device1 = new Device(1L, "Device1", "BrandA", LocalDateTime.now());
        device2 = new Device(2L, "Device2", "BrandB", LocalDateTime.now());
        deviceRepository.save(device1);
        deviceRepository.save(device2);
    }

    @Test
    public void testFindByBrand() {
        List<Device> foundDevices = deviceRepository.findByBrand("BrandA");
        assertThat(foundDevices).hasSize(1);
        assertThat(foundDevices.get(0)).isEqualTo(device1);
    }

    @Test
    public void testFindByBrandNotFound() {
        List<Device> foundDevices = deviceRepository.findByBrand("NonExistentBrand");
        assertThat(foundDevices).isEmpty();
    }

    @Test
    public void testSaveAndFindById() {
        Device savedDevice = deviceRepository.save(new Device(null, "Device3", "BrandC", LocalDateTime.now()));
        Device foundDevice = deviceRepository.findById(savedDevice.getId()).orElse(null);
        assertThat(foundDevice).isNotNull();
        assertThat(foundDevice.getName()).isEqualTo("Device3");
    }

    @Test
    public void testSaveWithNullName() {
        Device device = new Device(null, null, "BrandC", LocalDateTime.now());
        assertThatThrownBy(() -> deviceRepository.save(device))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testFindAll() {
        List<Device> allDevices = deviceRepository.findAll();
        assertThat(allDevices).hasSize(2);
    }

    @Test
    public void testFindAllEmpty() {
        deviceRepository.deleteAll();
        List<Device> allDevices = deviceRepository.findAll();
        assertThat(allDevices).isEmpty();
    }

    @Test
    public void testDeleteById() {
        deviceRepository.deleteById(device1.getId());
        List<Device> allDevices = deviceRepository.findAll();
        assertThat(allDevices).hasSize(1);
        assertThat(allDevices.get(0)).isEqualTo(device2);
    }

    @Test
    public void testDeleteByIdNotFound() {
        deviceRepository.deleteById(999L); // Non-existent ID
        List<Device> allDevices = deviceRepository.findAll();
        assertThat(allDevices).hasSize(2); // No device should be deleted
    }
}
