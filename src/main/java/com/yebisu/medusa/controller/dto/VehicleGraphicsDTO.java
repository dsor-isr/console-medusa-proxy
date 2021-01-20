package com.yebisu.medusa.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleGraphicsDTO {
    private String icon;
    private String path;
    private Boolean disableIcon;
    private Boolean pathPoints;
    private Boolean mdsPanel;
}
