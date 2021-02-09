package com.yebisu.medusa.service.mapper;

import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import com.yebisu.medusa.service.dto.VehicleState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Predicate;
import com.yebisu.medusa.proxy.rosmessage.dto.VAR;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class VehicleStateMapper {

    public VehicleState mapFom(final Content content) {
        if (content == null
                || content.getRosMessages() == null) {
            return new VehicleState();
        }

        final var vehicleState = new VehicleState();
        content.getRosMessages()
                .parallelStream()
                .flatMap(rosMessage -> Stream.of(rosMessage.getVars()))
                .flatMap(Collection::stream)
                .filter(varKeyNonNull().and(varDoubleNonNull()))
                .collect(Collectors.toMap(VAR::getKeyCoordination, VAR::getDoubleCoordination))
                .forEach((fieldAsKey, value) -> enrichVehicleState(vehicleState, fieldAsKey, value));
        return vehicleState;
    }

    private Predicate<VAR> varKeyNonNull() {
        return var -> var != null
                && var.getKeyCoordination() != null
                && !var.getKeyCoordination().isBlank();
    }

    private Predicate<VAR> varDoubleNonNull() {
        return var -> var != null
                && var.getDoubleCoordination() != null
                && !var.getDoubleCoordination().isBlank();
    }

    private void enrichVehicleState(final VehicleState vehicleState,
                                    final String fieldAsKey, final String value) {
        switch (fieldAsKey) {
            case "GPS_Good":
                vehicleState.setGpsGood(value);
                break;
            case "IMU_Good":
                vehicleState.setImuGood(value);
                break;
            case "Depth":
                vehicleState.setDepth(value);
                break;
            case "X":
                vehicleState.setGpsX(value);
                break;
            case "Y":
                vehicleState.setGpsY(value);
                break;
            case "Z":
                vehicleState.setGpsZ(value);
                break;
            case "Vx":
                vehicleState.setVX(value);
                break;
            case "Vy":
                vehicleState.setVY(value);
                break;
            case "Vz":
                vehicleState.setVZ(value);
                break;
            case "u":
                vehicleState.setU(value);
                break;
            case "Yaw":
                vehicleState.setYaw(value);
                break;
            case "Pitch_rate":
                vehicleState.setPitch(value);
                break;
            case "Roll":
                vehicleState.setRoll(value);
                break;
            case "yawRate":
                vehicleState.setYawRate(value);
                break;
            case "pitchRate":
                vehicleState.setPitchRate(value);
                break;
            case "Roll_rate":
                vehicleState.setRollRate(value);
                break;
            case "In_Press":
                vehicleState.setInPress(value);
                break;
            case "In_Press_dot":
                vehicleState.setInPressDot(value);
                break;
            case "battery_level":
                vehicleState.setBatteryLevel(value);
                break;
            case "altitude":
                vehicleState.setAltitude(value);
                break;
            case "status":
                vehicleState.setStatus(value);
                break;
            case "leaksUpper":
                vehicleState.setLeaksUpper(Boolean.getBoolean(value));
                break;
            case "leaksLower":
                vehicleState.setLeaksLower(Boolean.getBoolean(value));
                break;
            case "opMode":
                vehicleState.setOpMode(Integer.valueOf(value));
                break;
            default:
                break;
        }
    }
}
