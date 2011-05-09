package ru.swimmasters.domain;

import org.apache.commons.lang.builder.CompareToBuilder;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares athletes by their full name as recorded in the database.
 *
 * User: dedmajor
 * Date: 5/9/11
 */
public class EntryAthleteNameComparator implements Comparator<Entry>, Serializable {
    private static final long serialVersionUID = 3299994088413152194L;

    @Override
    public int compare(Entry o1, Entry o2) {
        return new CompareToBuilder()
                .append(o1.getAthlete().getFullName(), o2.getAthlete().getFullName())
                .toComparison();
    }
}
