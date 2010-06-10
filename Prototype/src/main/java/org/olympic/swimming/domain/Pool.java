package org.olympic.swimming.domain;

import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class Pool {

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    private Byte length;

    @NotNull
    @Min(2L)
    private Byte lanesCount;

}
