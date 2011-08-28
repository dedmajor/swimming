package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

/**
 * User: dedmajor
 * Date: 8/27/11
 */
@Entity
public class SwimMastersGroupRanking implements GroupRanking {
    @Id
    @GeneratedValue
    private Long id;

    private Integer place;

    @OneToOne(optional = false)
    @NotNull
    private SwimMastersResult result;

    @ManyToOne(optional = false)
    private SwimMastersAgeRanking ageRanking;

    SwimMastersGroupRanking() {
    }

    public SwimMastersGroupRanking(SwimMastersAgeRanking ageRanking, SwimMastersEntry entry) {
        if (!ageRanking.getEvent().getEntries().getAll().contains(entry)) {
            throw new IllegalArgumentException(
                    "event of age ranking " + ageRanking + " doesn't contain entry " + entry);
        }
        this.ageRanking = ageRanking;
        SwimMastersResult entryResult = entry.getResult();
        if (entryResult == null) {
            throw new IllegalArgumentException("entry " + entry + " has no result yet");
        }
        result = entryResult;
    }

    @Override
    public AgeRanking getAgeRanking() {
        return ageRanking;
    }

    @Override
    public Integer getPlace() {
        return place;
    }

    @NotNull
    @Override
    public SwimMastersResult getResult() {
        return result;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
