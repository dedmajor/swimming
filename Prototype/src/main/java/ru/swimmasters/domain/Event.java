package ru.swimmasters.domain;

import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import ru.swimmasters.domain.Meet;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import ru.swimmasters.domain.Discipline;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class Event {

    @NotNull
    @ManyToOne(targetEntity = Meet.class)
    @JoinColumn
    private Meet meet;

    @NotNull
    @ManyToOne(targetEntity = Discipline.class)
    @JoinColumn
    private Discipline discipline;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date holdingDate;
}
