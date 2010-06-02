package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * User: dedmajor
 * Date: May 23, 2010
 */
@Entity
public class Pool {
    @Id
    private Integer id;

    @NotNull
    private String location;

    @NotNull
    private Byte length;

    @NotNull
    @Min(2L)
    private Byte lanesCount;


    public Integer getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public Byte getLength() {
        return length;
    }

    public Byte getLanesCount() {
        return lanesCount;
    }
    

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("location", location).
                append("length", length).
                append("lanesCount", lanesCount).
                toString();
    }
}
