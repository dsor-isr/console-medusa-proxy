package com.yebisu.medusa.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class VehicleConfigurationDTO {
    private String id;
    @NotEmpty(message = "name is hardly required")
    private String name;
    @NotEmpty(message = "ipAddress is hardly required")
    private String ipAddress;
    @NotNull(message = "newVehicle is hardly required")
    private Boolean newVehicle;
    @NotNull(message = "vehicleResponseVariables are required")
    private List<vehicleResponseVariableDTO> vehicleResponseVariables;
    @NotNull(message = "coordinates are hardly required")
    CoordinatesDTO coordinates;
    @NotNull(message = "vehicleGraphics is hardly required")
    private VehicleGraphicsDTO vehicleGraphics;
    @NotNull(message = "vehicleDetailedInfo is hardly required")
    private VehicleDetailedInfoDTO vehicleDetailedInfo;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
