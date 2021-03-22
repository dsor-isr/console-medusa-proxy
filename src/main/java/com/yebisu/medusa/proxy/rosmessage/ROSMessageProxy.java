package com.yebisu.medusa.proxy.rosmessage;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import reactor.core.publisher.Mono;

public interface ROSMessageProxy {
    Content pingForROSMessageState(String ip);

    Mono<String> moveVehicleTo(String vehicleIP, Point point);
}
