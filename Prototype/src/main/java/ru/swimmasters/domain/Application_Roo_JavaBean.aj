package ru.swimmasters.domain;

import java.lang.Float;
import ru.swimmasters.domain.Athlete;
import ru.swimmasters.domain.Event;

privileged aspect Application_Roo_JavaBean {
    
    public Event Application.getEvent() {
        return this.event;
    }
    
    public void Application.setEvent(Event event) {
        this.event = event;
    }
    
    public Athlete Application.getContestant() {
        return this.contestant;
    }
    
    public void Application.setContestant(Athlete contestant) {
        this.contestant = contestant;
    }
    
    public Float Application.getDeclaredTime() {
        return this.declaredTime;
    }
    
    public void Application.setDeclaredTime(Float declaredTime) {
        this.declaredTime = declaredTime;
    }
    
}
