package com.yebisu.medusa.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleGraphics {
    private String icon;
    private String path;
    private Boolean disableIcon;
    private Boolean pathPoints;
    private Boolean mdsPanel;
}
