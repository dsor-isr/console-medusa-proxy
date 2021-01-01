package com.yebisu.medusa.service;

import com.yebisu.medusa.service.dto.VehicleState;

public interface VehicleService {
    VehicleState getState(String ip);
}
