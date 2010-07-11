package ru.swimmasters;

import org.joda.time.LocalDate;
import org.junit.Test;
import ru.swimmasters.domain.*;
import ru.swimmasters.validator.AthletesOrderValidator;
import ru.swimmasters.validator.GroupsOrderValidator;
import ru.swimmasters.validator.SingleAthleteValidator;

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

        new GroupsOrderValidator().validate(heats);
    }


    private Event makeOneGroupEvent() {
        Event event = new Event(new Meet().setPool(new Pool().setLanesCount(4)), new Discipline())
                .setHoldingDate(new LocalDate(2010, 04, 25));

        event
                .addApplication(new Application(event, new Athlete().setBirthYear(1980))
                        .setDeclaredTime(25.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1980))
                        .setDeclaredTime(24.5f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1980))
                        .setDeclaredTime(27.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1980))
                        .setDeclaredTime(24.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1978))
                        .setDeclaredTime(24.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1979).setName("Z"))
                        .setDeclaredTime(24.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1979).setName("C"))
                        .setDeclaredTime(24.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1979).setName("A"))
                        .setDeclaredTime(24.0f))
                .addApplication(new Application(event, new Athlete().setBirthYear(1979).setName("B"))
                        .setDeclaredTime(24.0f));


        return event;
    }

    @Test
    public void testAthletesOrder() {
        Event event = makeOneGroupEvent();
        List<Heat> heats = event.buildHeats(2);

        new AthletesOrderValidator().validate(heats);
    }

    @Test
    public void testSingleAthlete() {
        Event event = makeOneGroupEvent();
        List<Heat> heats = event.buildHeats(2);

        new SingleAthleteValidator().validate(heats);
    }
}
