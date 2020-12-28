package com.yebisu.medusa.proxy.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "content")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Content {
    private List<ROSMessage> rosMessages;


    public List<ROSMessage> getRosMessages() {
        if (rosMessages == null){
            rosMessages = new ArrayList<>();
        }
        return rosMessages;
    }

    @XmlElement(name = "ROSMessage")
    public void setRosMessages(List<ROSMessage> rosMessages) {
        this.rosMessages = rosMessages;
    }
}
