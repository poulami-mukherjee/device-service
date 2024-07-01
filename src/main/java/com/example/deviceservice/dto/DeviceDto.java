package com.example.deviceservice.dto;

import jakarta.validation.constraints.NotNull;

public record DeviceDto(
        @NotNull(message = "Name must not be null") String name,
        @NotNull(message = "Brand must not be null") String brand
) { }
