package com.example.deviceservice.service;

import com.example.deviceservice.dto.DeviceDto;
import com.example.deviceservice.exception.DeviceNotFoundException;
import com.example.deviceservice.exception.DeviceServiceException;
import com.example.deviceservice.model.Device;
import com.example.deviceservice.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private Device device;
    private DeviceDto deviceDto;

    @BeforeEach
    public void setUp() {
        device = new Device(1L, "Device1", "BrandA", LocalDateTime.now());
        deviceDto = new DeviceDto("Device1", "BrandA");
    }

    @Test
    public void testCreateDevice() {
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        Device createdDevice = deviceService.createDevice(deviceDto);

        assertThat(createdDevice).isNotNull();
        assertThat(createdDevice.getId()).isEqualTo(1L);
        assertThat(createdDevice.getName()).isEqualTo("Device1");
        assertThat(createdDevice.getBrand()).isEqualTo("BrandA");

        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    public void testCreateDeviceThrowsException() {
        when(deviceRepository.save(any(Device.class))).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> deviceService.createDevice(deviceDto))
                .isInstanceOf(DeviceServiceException.class)
                .hasMessageContaining("Error creating device");

        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    public void testFindDeviceById() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        Device foundDevice = deviceService.findDeviceById(1L);

        assertThat(foundDevice).isNotNull();
        assertThat(foundDevice.getId()).isEqualTo(1L);

        verify(deviceRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindDeviceByIdNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.findDeviceById(1L))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device not found");

        verify(deviceRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAllDevices() {
        when(deviceRepository.findAll()).thenReturn(Arrays.asList(device));

        List<Device> devices = deviceService.findAllDevices();

        assertThat(devices).isNotEmpty();
        assertThat(devices).hasSize(1);
        assertThat(devices.get(0)).isEqualTo(device);

        verify(deviceRepository, times(1)).findAll();
    }

    @Test
    public void testFindDevicesByBrand() {
        when(deviceRepository.findByBrand("BrandA")).thenReturn(Arrays.asList(device));

        List<Device> devices = deviceService.findDevicesByBrand("BrandA");

        assertThat(devices).isNotEmpty();
        assertThat(devices).hasSize(1);
        assertThat(devices.get(0).getBrand()).isEqualTo("BrandA");

        verify(deviceRepository, times(1)).findByBrand("BrandA");
    }

    @Test
    public void testUpdateDevice() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        Device updatedDevice = deviceService.updateDevice(1L, deviceDto);

        assertThat(updatedDevice).isNotNull();
        assertThat(updatedDevice.getId()).isEqualTo(1L);
        assertThat(updatedDevice.getName()).isEqualTo("Device1");
        assertThat(updatedDevice.getBrand()).isEqualTo("BrandA");

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    public void testUpdateDeviceNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.updateDevice(1L, deviceDto))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device not found");

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(0)).save(any(Device.class));
    }

    @Test
    public void testDeleteDevice() {
        when(deviceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(deviceRepository).deleteById(1L);

        deviceService.deleteDevice(1L);

        verify(deviceRepository, times(1)).existsById(1L);
        verify(deviceRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteDeviceNotFound() {
        when(deviceRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> deviceService.deleteDevice(1L))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device not found");

        verify(deviceRepository, times(1)).existsById(1L);
        verify(deviceRepository, times(0)).deleteById(1L);
    }

    @Test
    public void testDeleteDeviceThrowsException() {
        when(deviceRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(deviceRepository).deleteById(1L);

        assertThatThrownBy(() -> deviceService.deleteDevice(1L))
                .isInstanceOf(DeviceServiceException.class)
                .hasMessageContaining("Error deleting device");

        verify(deviceRepository, times(1)).existsById(1L);
        verify(deviceRepository, times(1)).deleteById(1L);
    }
}
