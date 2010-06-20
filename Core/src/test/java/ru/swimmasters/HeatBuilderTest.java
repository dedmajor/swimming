package ru.swimmasters;

import org.joda.time.LocalDate;
import org.junit.Test;
import ru.swimmasters.domain.*;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author dedmajor
 * @since 13.06.2010
 */
public class HeatBuilderTest {
    private Event makeTwoGroupsEvent() {
        Event event = new Event(new Meet().setPool(new Pool().setLanesCount(4)), new Discipline())
                .setHoldingDate(new LocalDate(2010, 04, 25));

        event
                .addApplication(new Application(event, new Athlete().setBirthYear(1980))
                        .setDeclaredTime(25.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1980))
                        .setDeclaredTime(27.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1979))
                        .setDeclaredTime(24.0f))

                .addApplication(new Application(event, new Athlete().setBirthYear(1985))
                        .setDeclaredTime(25.5f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1984))
                        .setDeclaredTime(24.5f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1984))
                        .setDeclaredTime(28.0f));

        return event;
    }

    @Test
    public void testLeads() {
        Event event = makeTwoGroupsEvent();
        List<Heat> heats = event.buildHeats(2);

        // TODO: make unified validator
        assertThat(heats.size(), equalTo(2));
        assertThat(heats.get(0).getAthletes().size(), equalTo(3));
        assertThat(heats.get(1).getAthletes().size(), equalTo(3));
    }

    @Test
    public void testGroupsOrder() {
        Event event = makeTwoGroupsEvent();
        List<Heat> heats = event.buildHeats(2);

        // TODO: make unified validator
        List<Athlete> firstHeatAthletes = heats.get(0).getAthletes();
        assertThat(AgeGroup.forAge(firstHeatAthletes.get(0).getAge(2010)), equalTo(AgeGroup.OVER_30));
        assertThat(AgeGroup.forAge(firstHeatAthletes.get(1).getAge(2010)), equalTo(AgeGroup.OVER_30));
        assertThat(AgeGroup.forAge(firstHeatAthletes.get(2).getAge(2010)), equalTo(AgeGroup.OVER_30));

        List<Athlete> secondHeatAthletes = heats.get(1).getAthletes();
        assertThat(AgeGroup.forAge(secondHeatAthletes.get(0).getAge(2010)), equalTo(AgeGroup.OVER_25));
        assertThat(AgeGroup.forAge(secondHeatAthletes.get(1).getAge(2010)), equalTo(AgeGroup.OVER_25));
        assertThat(AgeGroup.forAge(secondHeatAthletes.get(2).getAge(2010)), equalTo(AgeGroup.OVER_25));
    }


    private Event makeOneGroupEvent() {
        Event event = new Event(new Meet().setPool(new Pool().setLanesCount(4)), new Discipline())
                .setHoldingDate(new LocalDate(2010, 04, 25));

        event
                .addApplication(new Application(event, new Athlete().setBirthYear(1980))
                        .setDeclaredTime(25.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1980))
                        .setDeclaredTime(27.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1979))
                        .setDeclaredTime(24.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1979))
                        .setDeclaredTime(24.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1978))
                        .setDeclaredTime(23.0f));

        return event;
    }

    @Test
    public void testAthletesOrder() {
        Event event = makeOneGroupEvent();
        List<Heat> heats = event.buildHeats(2);

        // TODO: extract logic into validator

        Float previousFastestDeclaredTime = null;
        AgeGroup previousAgeGroup = null;

        for (Heat heat : heats) {
            Float fastestDeclaredTime = null;
            Float slowestDeclaredTime = null;

            for (Application application : heat.getApplications()) {
                if (fastestDeclaredTime == null || application.getDeclaredTime() < fastestDeclaredTime) {
                    fastestDeclaredTime = application.getDeclaredTime();
                }
                if (slowestDeclaredTime == null || application.getDeclaredTime() > slowestDeclaredTime) {
                    slowestDeclaredTime = application.getDeclaredTime();
                }
                if (previousAgeGroup != null) {
                    assertThat(application.getAgeGroup(), equalTo(previousAgeGroup));
                }
                previousAgeGroup = application.getAgeGroup();
            }

            if (previousFastestDeclaredTime != null) {
                assertThat(previousFastestDeclaredTime, greaterThanOrEqualTo(slowestDeclaredTime));
            }

            previousFastestDeclaredTime = fastestDeclaredTime;
        }
    }
}
