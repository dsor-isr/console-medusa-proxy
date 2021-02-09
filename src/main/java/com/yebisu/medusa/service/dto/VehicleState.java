package com.yebisu.medusa.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleState {
    private String gpsGood;
    private String imuGood;
    private String depth;
    private String gpsX;
    private String gpsY;
    private String gpsZ;
    private String vX;
    private String vY;
    private String vZ;
    private String u;
    private String yaw;
    private String pitch;
    private String roll;
    private String yawRate;
    private String pitchRate;
    private String rollRate;
    private String inPress;
    private String inPressDot;
    private String batteryLevel;
    private String altitude;
    private String status;
    private boolean leaksUpper;
    private boolean leaksLower;
    private Integer opMode;
}
