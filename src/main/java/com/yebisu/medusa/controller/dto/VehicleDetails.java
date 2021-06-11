package com.yebisu.medusa.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDetails {

    @NotEmpty(message = "vehicle id cannot be null")
    private String vehicleId;

    @NotEmpty(message = "velocity cannot be null")
    private Long velocity;
}
