package ru.swimmasters.domain;

/**
 * @author dedmajor
 * @since 29.07.2010
 */
public class Lane implements Comparable<Lane> {
    private final Integer number;

    public Lane(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo(Lane o) {
        return number.compareTo(o.getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lane lane = (Lane) o;

        if (!number.equals(lane.number)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public String toString() {
        return '#' + String.valueOf(number);
    }
}
