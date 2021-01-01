package com.yebisu.medusa.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class VehicleStateInputDto {
    @NotEmpty(message = "The vehicle IP is hardly required")
    private String vehicleIP;
}
