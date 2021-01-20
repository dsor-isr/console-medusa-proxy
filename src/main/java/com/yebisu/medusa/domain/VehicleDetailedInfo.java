package com.yebisu.medusa.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleDetailedInfo {
    private Boolean gps;
    private Boolean imu;
    private Boolean battery;
    private Boolean thrusters;
    private Boolean pressure;

}
