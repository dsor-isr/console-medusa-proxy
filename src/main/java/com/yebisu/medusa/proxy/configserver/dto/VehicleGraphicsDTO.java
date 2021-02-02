package com.yebisu.medusa.proxy.configserver.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VehicleGraphicsDTO {
    private String icon;
    private String path;
    private Boolean disableIcon;
    private Boolean pathPoints;
    private Boolean mdsPanel;
}
