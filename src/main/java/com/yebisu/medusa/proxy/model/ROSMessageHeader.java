package com.yebisu.medusa.proxy.model;

import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "ROSMessage")
@ToString
public class ROSMessageHeader {


    private List<VAR> vars;


    public List<VAR> getVars() {
        return vars;
    }

    @XmlElement(name = "VAR")
    public void setVars(List<VAR> vars) {
        this.vars = vars;
    }
}
