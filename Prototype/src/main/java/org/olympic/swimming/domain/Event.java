package org.olympic.swimming.domain;

import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import org.olympic.swimming.domain.Pool;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class Event {

    @NotNull
    private String name;

    @NotNull
    @ManyToOne(targetEntity = Pool.class)
    @JoinColumn
    private Pool pool;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date holdingDate;

}
