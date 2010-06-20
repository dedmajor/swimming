package ru.swimmasters.domain;

import java.lang.Integer;
import java.lang.String;
import ru.swimmasters.domain.Gender;

privileged aspect Discipline_Roo_JavaBean {
    
    public Gender Discipline.getGender() {
        return this.gender;
    }
    
    public void Discipline.setGender(Gender gender) {
        this.gender = gender;
    }
    
    public String Discipline.getName() {
        return this.name;
    }
    
    public void Discipline.setName(String name) {
        this.name = name;
    }
    
    public Integer Discipline.getDistance() {
        return this.distance;
    }
    
    public void Discipline.setDistance(Integer distance) {
        this.distance = distance;
    }
    
}
