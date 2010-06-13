package org.olympic.swimming;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.olympic.swimming.domain.*;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author dedmajor
 * @since 13.06.2010
 */
public class HeatBuilderTest {
    @Test
    public void testLeads() {
        Event event = new Event()
                .setPool(new Pool().setLanesCount((byte) 4))
                .setHoldingDate(new LocalDate(2010, 04, 25));

        event
                .addApplication(new Application(event, new Swimmer().setBirthYear(1980))
                        .setDeclaredTime(25.0f))
                .addApplication(new Application(event, new Swimmer().setBirthYear(1980))
                        .setDeclaredTime(27.0f))
                .addApplication(new Application(event, new Swimmer().setBirthYear(1979))
                        .setDeclaredTime(24.0f))

                .addApplication(new Application(event, new Swimmer().setBirthYear(1985))
                        .setDeclaredTime(25.5f))
                .addApplication(new Application(event, new Swimmer().setBirthYear(1984))
                        .setDeclaredTime(24.5f))
                .addApplication(new Application(event, new Swimmer().setBirthYear(1984))
                        .setDeclaredTime(28.0f));

        List<Heat> heats = event.buildHeats(2);

        assertThat(heats.size(), equalTo(2));
        assertThat(heats.get(0).getSwimmers().size(), equalTo(3));
        assertThat(heats.get(1).getSwimmers().size(), equalTo(3));

        List<Swimmer> firstHeatSwimmers = heats.get(0).getSwimmers();
        assertThat(AgeGroup.forAge(firstHeatSwimmers.get(0).getAge(2010)), equalTo(AgeGroup.OVER_30));
        assertThat(AgeGroup.forAge(firstHeatSwimmers.get(1).getAge(2010)), equalTo(AgeGroup.OVER_30));
        assertThat(AgeGroup.forAge(firstHeatSwimmers.get(2).getAge(2010)), equalTo(AgeGroup.OVER_30));

        List<Swimmer> secondHeatSwimmers = heats.get(1).getSwimmers();
        assertThat(AgeGroup.forAge(secondHeatSwimmers.get(0).getAge(2010)), equalTo(AgeGroup.OVER_25));
        assertThat(AgeGroup.forAge(secondHeatSwimmers.get(1).getAge(2010)), equalTo(AgeGroup.OVER_25));
        assertThat(AgeGroup.forAge(secondHeatSwimmers.get(2).getAge(2010)), equalTo(AgeGroup.OVER_25));
    }
}
