package ru.swimmasters.domain;

import java.util.Date;
import ru.swimmasters.domain.Discipline;
import ru.swimmasters.domain.Meet;

privileged aspect Event_Roo_JavaBean {
    
    public Meet Event.getMeet() {
        return this.meet;
    }
    
    public void Event.setMeet(Meet meet) {
        this.meet = meet;
    }
    
    public Discipline Event.getDiscipline() {
        return this.discipline;
    }
    
    public void Event.setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
    
    public Date Event.getHoldingDate() {
        return this.holdingDate;
    }
    
    public void Event.setHoldingDate(Date holdingDate) {
        this.holdingDate = holdingDate;
    }
    
}
