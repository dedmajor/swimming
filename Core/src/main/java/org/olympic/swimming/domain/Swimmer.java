package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

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
    private Short birthYear;


    public String getName() {
        return name;
    }

    public Short getBirthYear() {
        return birthYear;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("name", name).
                append("birthYear", birthYear).
                toString();
    }
}
