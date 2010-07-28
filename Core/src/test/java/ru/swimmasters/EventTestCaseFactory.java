package ru.swimmasters;

import org.joda.time.LocalDate;
import org.springframework.core.io.ClassPathResource;
import ru.swimmasters.domain.*;
import ru.swimmasters.jaxb.ContextHolder;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: dedmajor
 * Date: Jul 13, 2010
 */
public class EventTestCaseFactory {
    private final ContextHolder jaxbContextHolder;

    public EventTestCaseFactory() {
        jaxbContextHolder = new ContextHolder();
    }

    public Event makeOneGroupEvent() {
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

    public Event makeTwoGroupsEvent() {
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

    public Event makeRealEvent() {
        Unmarshaller um = jaxbContextHolder.createUnmarshaller();
        ClassPathResource classPathResource = new ClassPathResource("meetRegister1.xml");
        InputStream inputStream;
        try {
            inputStream = classPathResource.getInputStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            return ((MeetRegister) um.unmarshal(inputStream)).getMeet().getEvents().get(0);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }
}
