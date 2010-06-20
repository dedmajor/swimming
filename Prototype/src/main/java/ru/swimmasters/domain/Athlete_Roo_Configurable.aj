package ru.swimmasters.domain;

import org.springframework.beans.factory.annotation.Configurable;

privileged aspect Athlete_Roo_Configurable {
    
    declare @type: Athlete: @Configurable;
    
}
