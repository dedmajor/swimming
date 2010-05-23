package org.olympic.swimming.domain;

import java.lang.String;
import java.util.Date;

privileged aspect Swimmer_Roo_JavaBean {
    
    public String Swimmer.getName() {
        return this.name;
    }
    
    public void Swimmer.setName(String name) {
        this.name = name;
    }
    
    public Date Swimmer.getBirthDate() {
        return this.birthDate;
    }
    
    public void Swimmer.setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
}
