package ru.swimmasters.domain;

import org.hibernate.annotations.Type;
import org.joda.time.Duration;

import javax.persistence.*;

/**
 * LEN:
 *
 * Element RESULT
 * This element is used to describe one result of a swimmer or relay team.
 * Element/Attribute Type
 * Remarks
 * comment s - Additional comment e.g. for new records or reasons for
 *            disqualifications.
 * eventid n r Reference to the EVENT element using the id attribute.
 * heatid n - Reference to a heat (HEAT element in HEATS collection of the
 *           EVENT element).
 * lane n - The lane number of the entry.
 * points n - The number of points for the result according to the scoring table used
 *           in a meet.
 * reactiontime rt - The reaction time at the start. For relay events it is the reaction time of
 *                  the first swimmer.
 * RELAYPOSITIONS o - The information about relay swimmers in this result. Only allowed for
 *                   relay RESULT objects.
 * resultid n r Each result needs a unique id which should be unique over a meet.
 * status e - This attribute is used for the result status information. An empty status
 *           attribute means a regular result. The following values are allowed:
 *          * EXH: exhibition swim.
 *         * DSQ: athlete/relay disqualified.
 *        * DNS: athlete/relay did not start (no reason given or to late
 *       withdrawl).
 *      * DNF: athlete/relay did not finish.
 *     * WDR: athlete/relay was withdrawn (on time).
 * SPLITS o - The split times for the result. In a Lenex file, split times are always
 *           saved continuously.
 * swimtime st r The final time of the result in the swim time format.
 *
 * User: dedmajor
 * Date: 5/15/11
 */
@Entity
public class SwimMastersResult implements Result {
    @Id @GeneratedValue
    Long id;

    @OneToOne(optional = false)
    private SwimMastersEntry entry;

    @Column(nullable = false)
    @Type(type="org.joda.time.contrib.hibernate.PersistentDuration")
    private Duration swimTime;

    @Column(nullable = false)
    private ResultStatus status = ResultStatus.QUALIFIED;

    SwimMastersResult() {
    }

    public SwimMastersResult(SwimMastersEntry entry) {
        this.entry = entry;
    }

    public void update(FinalLaneResult finalLaneResult) {
        Integer lane = entry.getLane();
        assert lane != null;
        assert lane.equals(finalLaneResult.getLaneNumber());
        swimTime = finalLaneResult.getFinalTime();
        // TODO: status
        // TODO: lap results
    }

    @Override
    public MeetAthlete getAthlete() {
        return entry.getAthlete();
    }

    @Override
    public Event getEvent() {
        return entry.getEvent();
    }

    @Override
    public Heat getHeat() {
        Heat heat = entry.getHeat();
        if (heat == null) {
            throw new IllegalStateException();
        }
        return heat;
    }

    @Override
    public Integer getLane() {
        Integer lane = entry.getLane();
        if (lane == null) {
            throw new IllegalArgumentException();
        }
        return lane;
    }

    @Override
    public Duration getSwimTime() {
        return swimTime;
    }

    @Override
    public boolean isTimeAvailable() {
        return swimTime.isLongerThan(Duration.ZERO);
    }

    @Override
    public ResultStatus getStatus() {
        return status;
    }

    public void setSwimTime(Duration swimTime) {
        this.swimTime = swimTime;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}
