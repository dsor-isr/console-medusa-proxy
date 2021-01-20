package com.yebisu.medusa.controller.mapper;

import com.yebisu.medusa.controller.dto.VehicleConfigurationDTO;
import com.yebisu.medusa.domain.VehicleConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleConfiguration mapTo(VehicleConfigurationDTO vehicleConfigurationDTO);

    VehicleConfigurationDTO mapTo(VehicleConfiguration vehicleConfiguration);
}
