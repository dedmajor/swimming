package org.olympic.swimming.domain;

import org.springframework.beans.factory.annotation.Configurable;

privileged aspect Event_Roo_Configurable {
    
    declare @type: Event: @Configurable;
    
}
