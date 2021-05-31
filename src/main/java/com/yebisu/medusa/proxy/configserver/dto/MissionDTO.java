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
public class MissionDTO {

    private String id;
    private String name;
    private String type;
    private String color;
    private List<CoordinatesDTO> coordinates;
    private List<LineDTO> lines;
    private List<ArcDTO> arcDTOS;

}
