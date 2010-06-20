package ru.swimmasters.domain;

import java.lang.String;
import java.util.Date;
import ru.swimmasters.domain.Pool;

privileged aspect Event_Roo_JavaBean {
    
    public String Event.getName() {
        return this.name;
    }
    
    public void Event.setName(String name) {
        this.name = name;
    }
    
    public Pool Event.getPool() {
        return this.pool;
    }
    
    public void Event.setPool(Pool pool) {
        this.pool = pool;
    }
    
    public Date Event.getHoldingDate() {
        return this.holdingDate;
    }
    
    public void Event.setHoldingDate(Date holdingDate) {
        this.holdingDate = holdingDate;
    }
    
}
