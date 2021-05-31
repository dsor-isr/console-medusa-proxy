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
public class LineDTO {
    private Integer index;
    private List<CoordinatesDTO> coordinates;
}
