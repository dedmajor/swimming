package ru.swimmasters.domain;

import java.lang.String;

privileged aspect Discipline_Roo_ToString {
    
    public String Discipline.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Gender: ").append(getGender()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Distance: ").append(getDistance());
        return sb.toString();
    }
    
}
