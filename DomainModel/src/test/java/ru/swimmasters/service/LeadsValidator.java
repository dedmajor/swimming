package ru.swimmasters.service;

import ru.swimmasters.domain.AgeGroup;
import ru.swimmasters.domain.Entry;
import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;

import java.util.*;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * User: dedmajor
 * Date: Jul 13, 2010
 */
public class LeadsValidator implements StartListValidator {
    private final int leadsInAgeGroup;

    public LeadsValidator(int leadsInAgeGroup) {
        this.leadsInAgeGroup = leadsInAgeGroup;
    }

    @Override
    public void validateEntries(EventEntries entries) {
        List<Heat> heats = entries.getHeatsOrderedByNumber();
        Collection<AgeGroup> checkedGroups = new ArrayList<AgeGroup>();

        List<Heat> reverseHeats = reverseHeats(heats);

        Map<AgeGroup, Integer> totalAthletes = calculateTotalAthletes(reverseHeats);

        for (Heat heat : reverseHeats) {
            Map<AgeGroup, Integer> groupedHeatAthletes = calculateGroupedAthletes(heat);

            for (Map.Entry<AgeGroup, Integer> entry : groupedHeatAthletes.entrySet()) {
                AgeGroup group = entry.getKey();
                int expectedLeads = Math.min(leadsInAgeGroup, totalAthletes.get(group));

                if (!checkedGroups.contains(group)) {
                    assertThat("leads rule violated for age group " + group + " and heat " + heat.getNumber()
                            + " of " + reverseHeats.size() + ", groups: " + groupedHeatAthletes,
                            entry.getValue(), greaterThanOrEqualTo(expectedLeads));

                    checkedGroups.add(group);
                }
            }
        }
    }

    private static Map<AgeGroup, Integer> calculateGroupedAthletes(Heat heat) {
        HashMap<AgeGroup, Integer> groupedHeatAthletes
            = new HashMap<AgeGroup, Integer>();
        for (Entry entry : heat.getEntries().getAll()) {
            AgeGroup group = entry.getAgeGroup();
                groupedHeatAthletes.put(group,
                        groupedHeatAthletes.get(group) == null
                                ? 1
                                : groupedHeatAthletes.get(group) + 1);
        }
        return groupedHeatAthletes;
    }

    private static Map<AgeGroup, Integer> calculateTotalAthletes(List<Heat> heats) {
        HashMap<AgeGroup, Integer> totalAthletes
                = new HashMap<AgeGroup, Integer>();

        for (Heat heat : heats) {
            for (Entry entry : heat.getEntries().getAll()) {
                AgeGroup group = entry.getAgeGroup();
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
