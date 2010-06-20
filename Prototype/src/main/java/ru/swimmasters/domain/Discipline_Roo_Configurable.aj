package ru.swimmasters.domain;

import org.springframework.beans.factory.annotation.Configurable;

privileged aspect Discipline_Roo_Configurable {
    
    declare @type: Discipline: @Configurable;
    
}
