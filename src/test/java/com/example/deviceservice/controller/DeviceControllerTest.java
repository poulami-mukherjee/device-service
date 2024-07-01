package com.example.deviceservice.controller;

import com.example.deviceservice.dto.DeviceDto;
import com.example.deviceservice.exception.DeviceNotFoundException;
import com.example.deviceservice.model.Device;
import com.example.deviceservice.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    private Device device;
    private DeviceDto deviceDto;

    @BeforeEach
    public void setUp() {
        device = new Device(1L, "Device1", "BrandA", LocalDateTime.now());
        deviceDto = new DeviceDto("Device1", "BrandA");
    }

    @Test
    public void testAddDevice() throws Exception {
        when(deviceService.createDevice(any(DeviceDto.class))).thenReturn(device);

        mockMvc.perform(post("/devices/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Device1\",\"brand\":\"BrandA\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Device1"))
                .andExpect(jsonPath("$.brand").value("BrandA"));

        verify(deviceService, times(1)).createDevice(any(DeviceDto.class));
    }

    @Test
    public void testAddDeviceValidationFailure() throws Exception {
        mockMvc.perform(post("/devices/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"brand\":\"BrandA\"}"))
                .andExpect(status().isBadRequest());

        verify(deviceService, times(0)).createDevice(any(DeviceDto.class));
    }

    @Test
    public void testGetDeviceById() throws Exception {
        when(deviceService.findDeviceById(1L)).thenReturn(device);

        mockMvc.perform(get("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Device1"))
                .andExpect(jsonPath("$.brand").value("BrandA"));

        verify(deviceService, times(1)).findDeviceById(1L);
    }

    @Test
    public void testGetDeviceByIdNotFound() throws Exception {
        when(deviceService.findDeviceById(1L)).thenThrow(new DeviceNotFoundException(1L));

        mockMvc.perform(get("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deviceService, times(1)).findDeviceById(1L);
    }

    @Test
    public void testGetAllDevices() throws Exception {
        List<Device> devices = Arrays.asList(device);
        when(deviceService.findAllDevices()).thenReturn(devices);

        mockMvc.perform(get("/devices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Device1"))
                .andExpect(jsonPath("$[0].brand").value("BrandA"));

        verify(deviceService, times(1)).findAllDevices();
    }

    @Test
    public void testGetAllDevicesEmpty() throws Exception {
        when(deviceService.findAllDevices()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/devices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(deviceService, times(1)).findAllDevices();
    }

    @Test
    public void testSearchDevicesByBrand() throws Exception {
        List<Device> devices = Arrays.asList(device);
        when(deviceService.findDevicesByBrand("BrandA")).thenReturn(devices);

        mockMvc.perform(get("/devices/brand/BrandA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Device1"))
                .andExpect(jsonPath("$[0].brand").value("BrandA"));

        verify(deviceService, times(1)).findDevicesByBrand("BrandA");
    }

    @Test
    public void testUpdateDevice() throws Exception {
        when(deviceService.updateDevice(anyLong(), any(DeviceDto.class))).thenReturn(device);

        mockMvc.perform(put("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedDevice\",\"brand\":\"UpdatedBrand\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Device1"))
                .andExpect(jsonPath("$.brand").value("BrandA"));

        verify(deviceService, times(1)).updateDevice(anyLong(), any(DeviceDto.class));
    }

    @Test
    public void testUpdateDeviceNotFound() throws Exception {
        when(deviceService.updateDevice(anyLong(), any(DeviceDto.class))).thenThrow(new DeviceNotFoundException(anyLong()));

        mockMvc.perform(put("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedDevice\",\"brand\":\"UpdatedBrand\"}"))
                .andExpect(status().isNotFound());

        verify(deviceService, times(1)).updateDevice(anyLong(), any(DeviceDto.class));
    }

    @Test
    public void testPartialUpdateDevice() throws Exception {
        when(deviceService.findDeviceById(anyLong())).thenReturn(device);
        when(deviceService.updateDevice(anyLong(), any(DeviceDto.class))).thenReturn(device);

        mockMvc.perform(patch("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"PartiallyUpdatedDevice\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("PartiallyUpdatedDevice"))
                .andExpect(jsonPath("$.brand").value("BrandA"));

        verify(deviceService, times(1)).findDeviceById(anyLong());
        verify(deviceService, times(1)).updateDevice(anyLong(), any(DeviceDto.class));
    }

    @Test
    public void testDeleteDevice() throws Exception {
        doNothing().when(deviceService).deleteDevice(1L);

        mockMvc.perform(delete("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deviceService, times(1)).deleteDevice(1L);
    }

    @Test
    public void testDeleteDeviceNotFound() throws Exception {
        doThrow(new DeviceNotFoundException(1L)).when(deviceService).deleteDevice(1L);

        mockMvc.perform(delete("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deviceService, times(1)).deleteDevice(1L);
    }
}
