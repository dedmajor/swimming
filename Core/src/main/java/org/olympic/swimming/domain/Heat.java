package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dedmajor
 * @since 13.06.2010
 */
public class Heat {
    private final Pool pool;
    private List<Swimmer> swimmers; // TODO: FIXME: not a swimmers, but applications?

    public Heat(Pool pool) {
        this.pool = pool;
        swimmers = new ArrayList<Swimmer>(pool.getLanesCount());
    }

    public boolean hasMoreSpace() {
        return hasMoreSpace(1);
    }

    public boolean hasMoreSpace(int swimmersCount) {
        return swimmers.size() + swimmersCount <= pool.getLanesCount();
    }

    public void addSwimmer(Swimmer swimmer) {
        if (!hasMoreSpace()) {
            throw new IllegalStateException("no more space for swimmers");
        }
        swimmers.add(swimmer);
    }

    public List<Swimmer> getSwimmers() {
        return Collections.unmodifiableList(swimmers);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("swimmers.size", swimmers.size()).
                append("pool.id", pool.getId()).
                append("swimmers", swimmers).
                toString();
    }
}
