package org.olympic.swimming.domain;

import java.lang.String;

privileged aspect Pool_Roo_ToString {
    
    public String Pool.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Location: ").append(getLocation()).append(", ");
        sb.append("Length: ").append(getLength()).append(", ");
        sb.append("LanesCount: ").append(getLanesCount());
        return sb.toString();
    }
    
}
