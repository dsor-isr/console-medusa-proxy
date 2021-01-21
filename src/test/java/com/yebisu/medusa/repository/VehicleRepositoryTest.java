package com.yebisu.medusa.repository;

import com.yebisu.medusa.MedusaProxyApplication;
import com.yebisu.medusa.domain.VehicleConfiguration;
import com.yebisu.medusa.domain.VehicleDetailedInfo;
import com.yebisu.medusa.domain.VehicleGraphics;
import com.yebisu.medusa.domain.vehicleResponseVariable;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MedusaProxyApplication.class)
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;


    @Test
    public void givenVehicle_WhenSave_ThenCreateSave() {
        Mono<VehicleConfiguration> createVehicleConfigMono = vehicleRepository.save(buildVehicleConfig());

        StepVerifier.create(createVehicleConfigMono)
                .assertNext(vehicleConfig -> {
                    assertNotNull(vehicleConfig.getId());
                    assertEquals("172.18.18.177", vehicleConfig.getIpAddress());
                    assertNotNull(vehicleConfig.getVehicleGraphics());
                    assertEquals("Pilot", vehicleConfig.getVehicleGraphics().getIcon());
                })
                .expectComplete()
                .verify();
    }


    @Test
    public void givenId_whenFindById_ReturnIfExists() {
        Mono<VehicleConfiguration> vehicleConfigMono = vehicleRepository.save(buildVehicleConfig());
        Optional<VehicleConfiguration> optionalResult = vehicleConfigMono.blockOptional(Duration.ofMillis(50));

        assertNotEquals(Optional.empty(), optionalResult);
        Mono<VehicleConfiguration> foundVehicleConfigMono = vehicleRepository.findById(optionalResult.get().getId());

        StepVerifier.create(foundVehicleConfigMono)
                .assertNext(vehicleConfig -> {
                    assertEquals("LTS-MAZDA", vehicleConfig.getName());
                    assertEquals(Boolean.TRUE, vehicleConfig.getNewVehicle());
                    assertEquals("172.18.18.177", vehicleConfig.getIpAddress());
                }).expectComplete()
                .verify();

    }

    public void givenInvalidId_whenFindById_ThrowResourceNotFoundException() {

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
        vehicleConfiguration.setName("LTS-MAZDA");
        vehicleConfiguration.setNewVehicle(Boolean.TRUE);
        vehicleConfiguration.setIpAddress("172.18.18.177");
        vehicleConfiguration.setVehicleGraphics(vehicleGraphics);
        vehicleConfiguration.setVehicleDetailedInfo(vehicleDetailedInfo);
        vehicleConfiguration.getVehicleResponseVariables().add(vehicleResponseVariable);
        return vehicleConfiguration;
    }
}
