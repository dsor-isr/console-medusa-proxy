package com.yebisu.medusa.proxy.rosmessage;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ROSMessageProxy {
    Content pingForROSMessageState(String ip);

    Mono<ResponseEntity<Void>> moveVehicleTo(String vehicleIP, Point point);
}
