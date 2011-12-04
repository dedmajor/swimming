package ru.swimmasters.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * User: dedmajor
 * Date: 12/4/11
 */
@Entity
public class SwimMastersRelayPosition implements RelayPosition {
    @Id @GeneratedValue
    Long id;
    @ManyToOne
    SwimMastersRelayTeam relayTeam;
    // TODO: Meet athelte?
    @ManyToOne
    SwimMastersAthlete athlete;
    int number;

    public SwimMastersRelayPosition() {
    }

    public SwimMastersRelayPosition(SwimMastersRelayTeam relayTeam, SwimMastersAthlete athlete, int number) {
        this.relayTeam = relayTeam;
        this.athlete = athlete;
        this.number = number;
    }

    @Override
    public RelayTeam getRelayTeam() {
        return relayTeam;
    }

    @Override
    public Athlete getAthlete() {
        return athlete;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
