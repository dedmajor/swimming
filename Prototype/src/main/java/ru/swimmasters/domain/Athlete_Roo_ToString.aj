package ru.swimmasters.domain;

import java.lang.String;

privileged aspect Athlete_Roo_ToString {
    
    public String Athlete.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("BirthYear: ").append(getBirthYear());
        return sb.toString();
    }
    
}
