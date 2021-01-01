package com.yebisu.medusa.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDetail {
    private String title;
    private int status;
    private String detail;
    private long timeStamp;
    private String developerMessage;
    private Map<String, List<ValidationError>> errors;

    public Map<String, List<ValidationError>> getErrors() {
        if (errors == null) {
            errors = new HashMap<>();
        }
        return errors;
    }
}
