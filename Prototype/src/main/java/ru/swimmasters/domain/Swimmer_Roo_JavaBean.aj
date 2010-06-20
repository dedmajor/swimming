package ru.swimmasters.domain;

import java.lang.Short;
import java.lang.String;

privileged aspect Swimmer_Roo_JavaBean {
    
    public String Swimmer.getName() {
        return this.name;
    }
    
    public void Swimmer.setName(String name) {
        this.name = name;
    }
    
    public Short Swimmer.getBirthYear() {
        return this.birthYear;
    }
    
    public void Swimmer.setBirthYear(Short birthYear) {
        this.birthYear = birthYear;
    }
    
}
