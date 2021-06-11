package com.yebisu.medusa.proxy.configserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MissionDTO {

    private String id;
    private String name;
    private String type;
    private String color;
    private List<CoordinatesDTO> coordinates = new LinkedList<>();
    private List<LineDTO> lines  = new LinkedList<>();
    private List<ArcDTO> arcs = new LinkedList<>();

}
