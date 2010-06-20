package ru.swimmasters.domain;

import java.lang.String;

privileged aspect Meet_Roo_ToString {
    
    public String Meet.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Pool: ").append(getPool()).append(", ");
        sb.append("StartDate: ").append(getStartDate());
        return sb.toString();
    }
    
}
