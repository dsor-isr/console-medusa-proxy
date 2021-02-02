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
public class CoordinatesDTO {
    private Long x;
    private Long y;
    private Long z;
}
