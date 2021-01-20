package com.yebisu.medusa.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleDetailedInfoDTO {
    private Boolean gps;
    private Boolean imu;
    private Boolean battery;
    private Boolean thrusters;
    private Boolean pressure;

}
