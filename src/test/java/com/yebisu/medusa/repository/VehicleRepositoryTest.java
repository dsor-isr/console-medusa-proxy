package com.yebisu.medusa.repository;

import com.yebisu.medusa.MedusaProxyApplication;
import com.yebisu.medusa.domain.VehicleConfiguration;
import com.yebisu.medusa.domain.VehicleDetailedInfo;
import com.yebisu.medusa.domain.VehicleGraphics;
import com.yebisu.medusa.domain.vehicleResponseVariable;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MedusaProxyApplication.class)
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;


    @Test
    public void givenVehicle_WhenSave_ThenCreateSave() {
        Mono<VehicleConfiguration> createVehicleConfig = vehicleRepository.save(buildVehicleConfig());

        StepVerifier.create(createVehicleConfig)
                .assertNext(vehicleConfig -> {
                    assertNotNull(vehicleConfig.getId());
                    assertEquals("172.18.18.177", vehicleConfig.getIpAddress());
                    assertNotNull(vehicleConfig.getVehicleGraphics());
                    assertEquals("Pilot", vehicleConfig.getVehicleGraphics().getIcon());
                })
                .expectComplete()
                .verify();
    }

    private VehicleConfiguration buildVehicleConfig() {
        var vehicleResponseVariable = new vehicleResponseVariable();
        vehicleResponseVariable.setAsk(Boolean.FALSE);
        vehicleResponseVariable.setLabel("Fisher");

        VehicleGraphics vehicleGraphics = VehicleGraphics.builder().icon("Pilot")
                .disableIcon(Boolean.FALSE)
                .pathPoints(Boolean.TRUE)
                .build();

        VehicleDetailedInfo vehicleDetailedInfo = VehicleDetailedInfo.builder()
                .battery(Boolean.TRUE)
                .gps(Boolean.FALSE)
                .thrusters(Boolean.TRUE)
                .pressure(Boolean.TRUE)
                .build();

        var vehicleConfiguration = new VehicleConfiguration();
        vehicleConfiguration.setNewVehicle(Boolean.TRUE);
        vehicleConfiguration.setIpAddress("172.18.18.177");
        vehicleConfiguration.setVehicleGraphics(vehicleGraphics);
        vehicleConfiguration.setVehicleDetailedInfo(vehicleDetailedInfo);
        vehicleConfiguration.getVehicleResponseVariables().add(vehicleResponseVariable);
        return vehicleConfiguration;
    }
}
