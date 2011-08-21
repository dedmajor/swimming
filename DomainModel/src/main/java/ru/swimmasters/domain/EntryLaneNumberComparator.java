package ru.swimmasters.domain;

import java.io.Serializable;
import java.util.Comparator;

/**
 * User: dedmajor
 * Date: 8/21/11
 */
public class EntryLaneNumberComparator implements Comparator<Entry>, Serializable {
    private static final long serialVersionUID = -4226634197980480359L;

    @Override
    public int compare(Entry o1, Entry o2) {
        Integer lane = o1.getLane();
        assert lane != null;
        return lane.compareTo(o2.getLane());
    }
}
