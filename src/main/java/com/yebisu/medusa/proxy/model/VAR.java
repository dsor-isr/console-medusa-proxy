package com.yebisu.medusa.proxy.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@XmlRootElement(name = "VAR")
public class VAR {
    private String keyCoordination;
    private String timeCoordination;
    private String doubleCoordination;
    private String node;
    private String time;
    private String message;

    public String getKeyCoordination() {
        return keyCoordination;
    }

    @XmlElement(name = "KEY")
    public void setKeyCoordination(String keyCoordination) {
        this.keyCoordination = keyCoordination;
    }

    public String getTimeCoordination() {
        return timeCoordination;
    }

    @XmlElement(name = "TIME")
    public void setTimeCoordination(String timeCoordination) {
        this.timeCoordination = timeCoordination;
    }

    public String getDoubleCoordination() {
        return doubleCoordination;
    }

    @XmlElement(name = "DOUBLE")
    public void setDoubleCoordination(String doubleCoordination) {
        this.doubleCoordination = doubleCoordination;
    }

    public String getNode() {
        return node;
    }

    @XmlElement(name = "NODE")
    public void setNode(String node) {
        this.node = node;
    }

    public String getTime() {
        return time;
    }

    @XmlElement(name = "TIME")
    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    @XmlElement(name = "MSG")
    public void setMessage(String message) {
        this.message = message;
    }
}
