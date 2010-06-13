package org.olympic.swimming.domain;

/**
 * @author dedmajor
 * @since 13.06.2010
 */
public enum AgeGroup {
    OVER_15(15),
    OVER_20(20),
    OVER_25(25),
    OVER_30(30),
    OVER_35(35),
    OVER_40(40),
    OVER_45(45),
    OVER_50(50),
    OVER_55(55),
    OVER_60(60),
    OVER_65(65),
    OVER_70(70),
    OVER_75(75),
    OVER_80(80),
    OVER_85(85),
    OVER_90(90),
    OVER_95(95),
    OVER_100(100);

    private static final int YEARS_TO_NEXT_GROUP = 5;

    private int minAge;

    AgeGroup(int minAge) {
        this.minAge = minAge;
    }

    public static AgeGroup forAge(int age) {
        for (AgeGroup group : AgeGroup.values()) {
            if (group.isAgeIncluded(age)) {
                return group;
            }
        }

        throw new IllegalArgumentException("no group for age " + age);
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return minAge + YEARS_TO_NEXT_GROUP - 1;
    }

    public boolean isAgeIncluded(int age) {
        return age >= minAge && age <= getMaxAge();
    }
}
