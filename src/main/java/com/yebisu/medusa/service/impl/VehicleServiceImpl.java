package com.yebisu.medusa.service.impl;

import com.yebisu.medusa.proxy.ROSMessageProxy;
import com.yebisu.medusa.proxy.model.Content;
import com.yebisu.medusa.service.VehicleService;
import com.yebisu.medusa.service.dto.VehicleState;
import com.yebisu.medusa.service.mapper.VehicleStateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {
    private final ROSMessageProxy rosMessageProxy;
    private final VehicleStateMapper vehicleStateMapper;

    @Override
    public VehicleState getState(final String ip) {
        Content content = rosMessageProxy.pingForROSMessageState(ip);
        return vehicleStateMapper.mapFom(content);
    }
}
