package org.olympic.swimming.domain;

import java.util.Date;
import org.olympic.swimming.domain.Pool;

privileged aspect Event_Roo_JavaBean {
    
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
