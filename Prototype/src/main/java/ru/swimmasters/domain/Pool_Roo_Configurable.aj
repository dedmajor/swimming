package ru.swimmasters.domain;

import org.springframework.beans.factory.annotation.Configurable;

privileged aspect Pool_Roo_Configurable {
    
    declare @type: Pool: @Configurable;
    
}
