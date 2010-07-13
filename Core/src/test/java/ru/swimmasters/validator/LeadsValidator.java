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


        List<Heat> reverseHeats = reverseHeats(heats);

        EnumMap<AgeGroup, Integer> totalAthletes = calculateTotalAthletes(reverseHeats);

        for (Heat heat : reverseHeats) {
            EnumMap<AgeGroup, Integer> groupedHeatAthletes = calculateGroupedAthletes(heat);

            for (Map.Entry<AgeGroup, Integer> entry : groupedHeatAthletes.entrySet()) {
                AgeGroup group = entry.getKey();
                int expectedLeads = Math.min(leadsInAgeGroup, totalAthletes.get(group));

                if (!checkedGroups.contains(group)) {
                    assertThat("leads rule violated for age group " + group + " and heat " + heat,
                            entry.getValue(), greaterThanOrEqualTo(expectedLeads));

                    checkedGroups.add(group);
                }
            }
        }
    }

    private static EnumMap<AgeGroup, Integer> calculateGroupedAthletes(Heat heat) {
        EnumMap<AgeGroup, Integer> groupedHeatAthletes
            = new EnumMap<AgeGroup, Integer>(AgeGroup.class);
        for (Application application : heat.getApplications()) {
            AgeGroup group = application.getAgeGroup();
                groupedHeatAthletes.put(group,
                        groupedHeatAthletes.get(group) == null
                                ? 1
                                : groupedHeatAthletes.get(group) + 1);
        }
        return groupedHeatAthletes;
    }

    private static EnumMap<AgeGroup, Integer> calculateTotalAthletes(List<Heat> heats) {
        EnumMap<AgeGroup, Integer> totalAthletes
                = new EnumMap<AgeGroup, Integer>(AgeGroup.class);

        for (Heat heat : heats) {
            for (Application application : heat.getApplications()) {
                AgeGroup group = application.getAgeGroup();
                totalAthletes.put(group,
                        totalAthletes.get(group) == null
                                ? 1
                                : totalAthletes.get(group) + 1);
            }
        }

        return totalAthletes;
    }

    private static List<Heat> reverseHeats(List<Heat> heats) {
        List<Heat> reverseHeats = new ArrayList<Heat>(heats);
        Collections.reverse(reverseHeats);
        return reverseHeats;
    }
}
