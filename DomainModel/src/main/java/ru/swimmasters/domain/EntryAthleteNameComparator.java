package ru.swimmasters.domain;

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
    private static final Comparator<Athlete> ATHLETE_COMPARATOR = SwimMastersAthlete.getNameComparator();

    @Override
    public int compare(Entry o1, Entry o2) {
        return ATHLETE_COMPARATOR.compare(o1.getAthlete().getAthlete(), o2.getAthlete().getAthlete());
    }
}
