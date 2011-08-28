package ru.swimmasters.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: dedmajor
 * Date: 8/27/11
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"event", "age_group"}))
public class SwimMastersAgeRanking implements AgeRanking {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private SwimMastersEvent event;

    @ManyToOne(optional = false)
    private SwimMastersAgeGroup ageGroup;

    @OneToMany(mappedBy = "ageRanking")
    private final List<SwimMastersGroupRanking> groupRankings = new ArrayList<SwimMastersGroupRanking>();

    SwimMastersAgeRanking() {
        // hibernate shall pass
    }

    public SwimMastersAgeRanking(SwimMastersEvent event, SwimMastersAgeGroup ageGroup) {
        this.event = event;
        this.ageGroup = ageGroup;
    }

    @Override
    public SwimMastersEvent getEvent() {
        return event;
    }

    @Override
    public SwimMastersAgeGroup getAgeGroup() {
        return ageGroup;
    }

    @Override
    public GroupRankings getGroupRankings() {
        return new GroupRankings() {
            @Override
            public List<GroupRanking> getAll() {
                return Collections.<GroupRanking>unmodifiableList(groupRankings);
            }
        };
    }

    public void setGroupRankings(List<SwimMastersGroupRanking> rankings) {
        groupRankings.clear();
        groupRankings.addAll(rankings);
    }
}
