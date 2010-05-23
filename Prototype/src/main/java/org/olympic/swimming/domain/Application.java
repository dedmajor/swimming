package org.olympic.swimming.domain;

import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import org.olympic.swimming.domain.Event;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Digits;
import org.olympic.swimming.domain.Swimmer;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class Application {

    @NotNull
    @ManyToOne(targetEntity = Event.class)
    @JoinColumn
    private Event event;

    @NotNull
    @ManyToOne(targetEntity = Swimmer.class)
    @JoinColumn
    private Swimmer contestant;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private Float declaredTime;

}
