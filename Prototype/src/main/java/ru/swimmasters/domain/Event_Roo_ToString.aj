package ru.swimmasters.domain;

import java.lang.String;

privileged aspect Event_Roo_ToString {
    
    public String Event.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Meet: ").append(getMeet()).append(", ");
        sb.append("Discipline: ").append(getDiscipline()).append(", ");
        sb.append("HoldingDate: ").append(getHoldingDate());
        return sb.toString();
    }
    
}
