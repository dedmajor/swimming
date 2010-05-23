package org.olympic.swimming.domain;

import org.springframework.beans.factory.annotation.Configurable;

privileged aspect Pool_Roo_Configurable {
    
    declare @type: Pool: @Configurable;
    
}
