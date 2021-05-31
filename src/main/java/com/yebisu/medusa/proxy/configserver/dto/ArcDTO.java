package com.yebisu.medusa.proxy.configserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ArcDTO {
    private Integer index;
    private Integer direction;
    private Double radius;
    private List<CoordinatesDTO> coordinates;
}
