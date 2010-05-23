package org.olympic.swimming.domain;

import java.lang.Float;
import org.olympic.swimming.domain.Event;
import org.olympic.swimming.domain.Swimmer;

privileged aspect Application_Roo_JavaBean {
    
    public Event Application.getEvent() {
        return this.event;
    }
    
    public void Application.setEvent(Event event) {
        this.event = event;
    }
    
    public Swimmer Application.getContestant() {
        return this.contestant;
    }
    
    public void Application.setContestant(Swimmer contestant) {
        this.contestant = contestant;
    }
    
    public Float Application.getDeclaredTime() {
        return this.declaredTime;
    }
    
    public void Application.setDeclaredTime(Float declaredTime) {
        this.declaredTime = declaredTime;
    }
    
}
