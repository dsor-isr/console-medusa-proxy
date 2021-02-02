package com.yebisu.medusa.proxy.configserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VehicleDetailedInfoDTO {
    private Boolean gps;
    private Boolean imu;
    private Boolean battery;
    private Boolean thrusters;
    private Boolean pressure;

}
