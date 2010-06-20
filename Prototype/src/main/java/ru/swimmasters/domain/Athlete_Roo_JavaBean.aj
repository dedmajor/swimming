package ru.swimmasters.domain;

import java.lang.Short;
import java.lang.String;

privileged aspect Athlete_Roo_JavaBean {
    
    public String Athlete.getName() {
        return this.name;
    }
    
    public void Athlete.setName(String name) {
        this.name = name;
    }
    
    public Short Athlete.getBirthYear() {
        return this.birthYear;
    }
    
    public void Athlete.setBirthYear(Short birthYear) {
        this.birthYear = birthYear;
    }
    
}
