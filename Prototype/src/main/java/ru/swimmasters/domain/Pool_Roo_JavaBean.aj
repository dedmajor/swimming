package ru.swimmasters.domain;

import java.lang.Byte;
import java.lang.String;

privileged aspect Pool_Roo_JavaBean {
    
    public String Pool.getName() {
        return this.name;
    }
    
    public void Pool.setName(String name) {
        this.name = name;
    }
    
    public String Pool.getLocation() {
        return this.location;
    }
    
    public void Pool.setLocation(String location) {
        this.location = location;
    }
    
    public Byte Pool.getLength() {
        return this.length;
    }
    
    public void Pool.setLength(Byte length) {
        this.length = length;
    }
    
    public Byte Pool.getLanesCount() {
        return this.lanesCount;
    }
    
    public void Pool.setLanesCount(Byte lanesCount) {
        this.lanesCount = lanesCount;
    }
    
}
