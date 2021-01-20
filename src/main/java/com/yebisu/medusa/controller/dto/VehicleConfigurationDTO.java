package com.yebisu.medusa.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class VehicleConfigurationDTO {
    private String id;
    private String name;
    private String ipAddress;
    private Boolean newVehicle;
    private List<vehicleResponseVariableDTO> vehicleResponseVariables;
    private List<CoordinatesDTO> coordinates;
    private VehicleGraphicsDTO vehicleGraphics;
    private VehicleDetailedInfoDTO vehicleDetailedInfo;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
