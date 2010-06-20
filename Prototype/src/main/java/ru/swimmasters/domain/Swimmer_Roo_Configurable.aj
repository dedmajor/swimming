package ru.swimmasters.domain;

import org.springframework.beans.factory.annotation.Configurable;

privileged aspect Swimmer_Roo_Configurable {
    
    declare @type: Swimmer: @Configurable;
    
}
