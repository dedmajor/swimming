package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 12/4/11
 */
public class SwimMastersRelayPosition implements RelayPosition {
    SwimMastersRelayTeam team;
    SwimMastersAthlete athlete;
    int number;

    public SwimMastersRelayPosition() {
    }

    public SwimMastersRelayPosition(SwimMastersRelayTeam team, SwimMastersAthlete athlete, int number) {
        this.team = team;
        this.athlete = athlete;
        this.number = number;
    }

    @Override
    public RelayTeam getRelayTeam() {
        return team;
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
