package com.yebisu.medusa.proxy.model;

import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ROSMessage")
@ToString
public class ROSMessage {

private ROSMessageHeader rosMessageHeader;
private List<VAR> vars;

    public ROSMessageHeader getRosMessageHeader() {
        return rosMessageHeader;
    }

    @XmlElement(name = "ROSMessage")
    public void setRosMessageHeader(ROSMessageHeader rosMessageHeader) {
        this.rosMessageHeader = rosMessageHeader;
    }

    public List<VAR> getVars() {
        if (vars == null){
            vars = new ArrayList<>();
        }
        return vars;
    }

    @XmlElement(name = "VAR")
    public void setVars(List<VAR> vars) {
        this.vars = vars;
    }
}
