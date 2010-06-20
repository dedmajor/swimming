package ru.swimmasters.domain;

import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import ru.swimmasters.domain.Gender;
import javax.validation.constraints.NotNull;
import javax.persistence.Enumerated;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class Discipline {

    @NotNull
    @Enumerated
    private Gender gender;

    @NotNull
    private String name;

    @NotNull
    private Integer distance;
}
