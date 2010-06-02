package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * User: dedmajor
 * Date: Jun 2, 2010
 */
@Entity
public class Swimmer {
    @Id
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    private LocalDate birthDate;


    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("name", name).
                append("birthDate", birthDate).
                toString();
    }
}
