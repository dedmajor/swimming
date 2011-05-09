package ru.swimmasters.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
@Entity
public class SwimMastersPool implements Pool {
    /*
        SwimMasters:

        id       | integer                 | not null default nextval('venue_id_seq'::regclass)
        city_id  | bigint                  | not null
        name     | character varying(128)  |
        address  | character varying(256)  |
        course| smallint                | not null
        lanes    | smallint                | not null
        map_link | character varying(4096) |

        TODO: course should be moved to the Meet.

        Len:

        Element POOL
        This element is used to describe the pool where the meet took place.
        Element/Attribute Type
        name s - The name of the venue (e.g. "Aquatic Center").
        lanemax n - Number of the last lane used in the pool for the meet. The number of
                   lanes can be calculated with LANEMAX - LANEMIN + 1.
        lanemin n - Number of the first lane used in the pool for the meet.
        temperature n - The water temperature.
        type e - The type of the pool. Acceptable values are INDOOR, OUTDOOR,
                LAKE and OCEAN.
     */
    @Id
    Integer id;

    @Column(nullable = false)
    private int laneMin;

    @Column(nullable = false)
    private int laneMax;

    String name;

    SwimMastersPool() {
    }

    public SwimMastersPool(int laneMin, int laneMax) {
        this.laneMin = laneMin;
        this.laneMax = laneMax;
    }

    public void setLaneMin(int laneMin) {
        this.laneMin = laneMin;
    }

    public void setLaneMax(int laneMax) {
        this.laneMax = laneMax;
    }

    @Override
    public int getLaneMin() {
        return laneMin;
    }

    @Override
    public int getLaneMax() {
        return laneMax;
    }

    @Override
    public int getMeetLanesCount() {
        return laneMax - laneMin + 1;
    }

    @Override
    public boolean isMeetLane(int laneNumber) {
        return laneNumber <= laneMax && laneNumber >= laneMin;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
