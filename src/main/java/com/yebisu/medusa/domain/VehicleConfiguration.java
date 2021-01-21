package com.yebisu.medusa.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class VehicleConfiguration {

    @Id
    private String id;
    private String name;
    private String ipAddress;
    private Boolean newVehicle;
    private List<vehicleResponseVariable> vehicleResponseVariables;
    private Coordinates coordinates;
    private VehicleGraphics vehicleGraphics;
    private VehicleDetailedInfo vehicleDetailedInfo;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public List<vehicleResponseVariable> getVehicleResponseVariables() {
        if (vehicleResponseVariables == null){
            vehicleResponseVariables = new ArrayList<>();
        }
        return vehicleResponseVariables;
    }
}
