package ru.swimmasters.domain;

import java.lang.String;

privileged aspect Application_Roo_ToString {
    
    public String Application.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Event: ").append(getEvent()).append(", ");
        sb.append("Participant: ").append(getParticipant()).append(", ");
        sb.append("DeclaredTime: ").append(getDeclaredTime());
        return sb.toString();
    }
    
}
