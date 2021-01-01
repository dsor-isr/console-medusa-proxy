package com.yebisu.medusa.exception.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ValidationError {
    private String code;
    private String message;
}
