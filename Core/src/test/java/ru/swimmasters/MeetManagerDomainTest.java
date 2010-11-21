package ru.swimmasters;

import org.joda.time.LocalDate;
import org.junit.Test;
import ru.swimmasters.domain.*;

import static org.junit.Assert.*;

/**
 * Tests common data structures.
 *
 * TODO: switch to interfaces.
 *
 * User: dedmajor
 * Date: Nov 14, 2010
 */
public class MeetManagerDomainTest {
    private final Meet meet;
    private final Event event;
    private final Pool pool;

    public MeetManagerDomainTest() {
        meet = new Meet("XIX Чемпионат России 2010 г. по плаванию в категории «Мастерс»");
        meet.setStartDate(new LocalDate("2010-04-23"));
        pool = new Pool("Архимед", "г. Дубна");
        meet.setPool(pool);

        event = new Event(new Discipline(Gender.WOMEN, 800, "вольный стиль"), new LocalDate("2010-04-23"));
        meet.addEvent(event);
    }

    @Test
    public void simpleProcess() {
        Application firstApplication = new Application(event, new Athlete("Никулина Анна", 1984, Gender.WOMEN));
        Application secondApplication = new Application(event, new Athlete("Коробейкина Ольга", 1959, Gender.WOMEN));

        event.addApplication(firstApplication);
        event.addApplication(secondApplication);

        assertEquals("event contains two applications", 2, event.getApplications().size());

        StartList startList = new StartList(event);
        startList.addHeats(event.buildHeats(3));

        TotalRanking totalRanking = new TotalRanking(event);

        assertEquals("the only heat", 1, startList.getHeats().size());
        for (Heat heat : startList.getHeats()) {
            assertTrue("heat must contain first application", heat.containsApplication(firstApplication));
            assertTrue("heat must contain second application", heat.containsApplication(secondApplication));
            Result firstResult = new Result(firstApplication);
            firstResult.setHeat(heat);
            Result secondResult = new Result(secondApplication);
            secondResult.setHeat(heat);
            secondResult.setResult(ResultCode.FINISHED, 0.42f);

            totalRanking.addResult(firstResult);
            totalRanking.addResult(secondResult);
        }

        Result firstResult = totalRanking.getAbsoluteResults().first();
        Result secondResult = totalRanking.getAbsoluteResults().last();

        assertEquals(secondApplication, firstResult.getApplication());
        assertEquals(ResultCode.FINISHED, firstResult.getCode());
        assertEquals(1, firstResult.getHeatNumber());
        assertEquals(5, firstResult.getLaneNumber());

        assertEquals(firstApplication, secondResult.getApplication());
        assertEquals(ResultCode.DNS, secondResult.getCode());
        assertEquals(1, secondResult.getHeatNumber());
        assertEquals(4, secondResult.getLaneNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void genderMismatch() {
        //noinspection ResultOfObjectAllocationIgnored
        new Application(event, new Athlete("Корнеев Андрей", 1959, Gender.MEN));
        fail("expecting failure due to mismatched gender");
    }
}
