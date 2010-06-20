package ru.swimmasters.domain;

import java.lang.String;
import java.util.Date;
import ru.swimmasters.domain.Pool;

privileged aspect Meet_Roo_JavaBean {
    
    public String Meet.getName() {
        return this.name;
    }
    
    public void Meet.setName(String name) {
        this.name = name;
    }
    
    public Pool Meet.getPool() {
        return this.pool;
    }
    
    public void Meet.setPool(Pool pool) {
        this.pool = pool;
    }
    
    public Date Meet.getStartDate() {
        return this.startDate;
    }
    
    public void Meet.setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
}
