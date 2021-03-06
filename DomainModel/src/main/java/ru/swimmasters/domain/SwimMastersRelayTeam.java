package ru.swimmasters.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: dedmajor
 * Date: 12/4/11
 */
@Entity
public class SwimMastersRelayTeam implements RelayTeam {
    @ManyToOne
    SwimMastersMeet meet;
    @Id
    Long id;
    String name;

    @OneToMany(mappedBy = "relayTeam")
    List<SwimMastersRelayPosition> positions = new ArrayList<SwimMastersRelayPosition>();
    // TODO: match index and number?

    public SwimMastersRelayTeam() {
    }

    public SwimMastersRelayTeam(SwimMastersMeet meet) {
        this.meet = meet;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Meet getMeet() {
        return meet;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RelayPositions getRelayPositions() {
        return new RelayPositions() {
            @Override
            public RelayTeam getRelayTeam() {
                return SwimMastersRelayTeam.this;
            }

            @Override
            public List<RelayPosition> getAll() {
                return Collections.unmodifiableList((List<? extends RelayPosition>) positions);
            }
        };
    }

    public SwimMastersRelayPosition newRelayPosition(SwimMastersAthlete athlete, int number) {
        SwimMastersRelayPosition result = new SwimMastersRelayPosition(this, athlete, number);
        positions.add(result);
        return result;
    }

    @Override
    public int getSize() {
        return positions.size();
    }

    @Override
    public RelayEntries getRelayEntries() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RelayResults getRelayResults() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
