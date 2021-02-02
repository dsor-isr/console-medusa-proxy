package com.yebisu.medusa.proxy.configserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VehicleConfigurationDTO {
    private String name;
    private String ipAddress;
    private List<vehicleResponseVariableDTO> vehicleResponseVariables;
    private CoordinatesDTO waypointKeybindJ;
    private CoordinatesDTO waypointKeybindK;
    private VehicleGraphicsDTO vehicleGraphics;
    private VehicleDetailedInfoDTO vehicleDetailedInfo;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
