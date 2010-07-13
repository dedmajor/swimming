package ru.swimmasters;

import org.junit.Test;
import ru.swimmasters.domain.Event;
import ru.swimmasters.domain.Heat;
import ru.swimmasters.validator.*;

import java.util.List;

/**
 * @author dedmajor
 * @since 13.06.2010
 */
public class HeatBuilderTest {
    @Test
    public void testEventFactory() {
        EventTestCaseFactory testCaseFactory = new EventTestCaseFactory();

        checkEvent(testCaseFactory.makeOneGroupEvent());
        checkEvent(testCaseFactory.makeTwoGroupsEvent());
        checkEvent(testCaseFactory.makeRealEvent());
    }


    private static void checkEvent(Event event) {
        for (int i = 1; i <= event.getPool().getLanesCount() - 1; i++) {
            System.out.println(i); // TODO: test context?
            checkEvent(event, i);
        }
    }


    private static void checkEvent(Event event, int inAgeGroup) {
        List<Heat> heats = event.buildHeats(inAgeGroup);

        new EmptyHeatsValidator().validate(heats);
        new GroupsOrderValidator().validate(heats);
        new AthletesOrderValidator().validate(heats);
        new SingleAthleteValidator().validate(heats);
        new LeadsValidator(inAgeGroup).validate(heats);
    }
}
