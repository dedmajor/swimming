package ru.swimmasters.validator;

import ru.swimmasters.domain.AgeGroup;
import ru.swimmasters.domain.Application;
import ru.swimmasters.domain.Heat;

import java.util.*;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * User: dedmajor
 * Date: Jul 13, 2010
 */
public class LeadsValidator implements HeatsValidator {
    private final int leadsInAgeGroup;

    public LeadsValidator(int leadsInAgeGroup) {
        this.leadsInAgeGroup = leadsInAgeGroup;
    }

    @Override
    public void validate(List<Heat> heats) {
        Collection<AgeGroup> checkedGroups = EnumSet.noneOf(AgeGroup.class);
        EnumMap<AgeGroup, Integer> groupedHeatAthletes
                = new EnumMap<AgeGroup, Integer>(AgeGroup.class);

        List<Heat> reverseHeats = reverseHeats(heats);

        for (Heat heat : reverseHeats) {
            groupedHeatAthletes.clear();

            for (Application application : heat.getApplications()) {
                AgeGroup group = application.getAgeGroup();
                if (!checkedGroups.contains(group)) {
                    groupedHeatAthletes.put(group,
                            groupedHeatAthletes.get(group) == null
                                    ? 1
                                    : groupedHeatAthletes.get(group) + 1);
                }
            }

            for (Map.Entry<AgeGroup, Integer> entry : groupedHeatAthletes.entrySet()) {
                assertThat("leads rule violated for age group " + entry.getKey() + " and heat " + heat,
                        entry.getValue(), greaterThanOrEqualTo(leadsInAgeGroup));
                checkedGroups.add(entry.getKey());
            }
        }
    }

    private static List<Heat> reverseHeats(List<Heat> heats) {
        List<Heat> reverseHeats = new ArrayList<Heat>(heats);
        Collections.reverse(reverseHeats);
        return reverseHeats;
    }
}
