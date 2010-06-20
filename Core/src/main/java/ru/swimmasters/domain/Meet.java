package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author dedmajor
 * @since 20.06.2010
 */
@Entity
@Table(name = "MEET")
public class Meet {
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne(targetEntity = Pool.class)
    private Pool pool;

    @NotNull
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    private LocalDate startDate;


    public String getName() {
        return name;
    }

    public Meet setName(String name) {
        this.name = name;
        return this;
    }

    public Pool getPool() {
        return pool;
    }

    public Meet setPool(Pool pool) {
        this.pool = pool;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Meet setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("name", name).
                append("pool", pool).
                toString();
    }
}