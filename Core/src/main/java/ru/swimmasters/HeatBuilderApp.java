package ru.swimmasters;

import org.joda.time.LocalDate;
import ru.swimmasters.domain.*;
import ru.swimmasters.jaxb.ContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.Collections;
import java.util.List;

/**
 * User: dedmajor
 * Date: May 23, 2010
 */
public class HeatBuilderApp {
    public static void main(String args[]) {
        // integration();

        run(args);
    }

    private static void integration() {
        /*
       TODO: remove
        */
        /*
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        final EntityManagerFactoryInfo entityManagerFactory =
                (EntityManagerFactoryInfo) context.getBean("entityManagerFactory");
                */

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
        //EntityManagerFactory emf = entityManagerFactory.getNativeEntityManagerFactory();

        EntityManager em = emf.createEntityManager();


        //createAndSave(em);
        dumpEvents(em);

        em.close();
        emf.close();
    }

    private static void run(String[] args) {
        if (args.length < 1) {
            usage();
            return;
        }

        if (args[0].equals("dumpDb")) {

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
            EntityManager em = emf.createEntityManager();

            xmlDumpEvents(em);
        } else if (args[0].equals("build")) {
            xmlLoadEvents();
        } else {
            usage();
        }
    }

    private static void usage() {
        System.err.println("Usage: ");
        System.err.println("HeatBuilderApp dumpDb > event.xml");
        System.err.println("HeatBuilderApp build < event.xml");
    }

    private static void xmlLoadEvents() {
        Unmarshaller um = new ContextHolder().createUnmarshaller();
        Event event;
        try {
            event = (Event) um.unmarshal(System.in);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
        dumpEvent(event);
    }

    private static void createAndSave(EntityManager em) {
        // TODO: extract into integration test
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Pool pool = new Pool()
                .setLanesCount(9)
                .setName("name")
                .setLocation("location");
        Athlete participant = new Athlete().setBirthYear(1985).setName("name");
        Meet meet = new Meet().setPool(pool).setName("name").setStartDate(new LocalDate("2010-04-25"));
        Discipline discipline = new Discipline()
                .setDistance(50)
                .setGender(Gender.MEN)
                .setName("name");
        Event event1 = new Event(meet, discipline)
                .setHoldingDate(new LocalDate("2010-04-25"));
        Application a = new Application(event1, participant)
                .setDeclaredTime(20.5f);
        em.persist(pool);
        em.persist(meet);
        em.persist(discipline);
        em.persist(participant);
        em.persist(event1);
        em.persist(a);

        transaction.commit();
    }

    private static void dumpEvents(EntityManager em) {
        @SuppressWarnings({"unchecked"}) // JPA
                List<Event> events = (List<Event>) Collections.checkedList(
                em.createQuery("from Event").getResultList(), Event.class);

        for (Event event : events) {
            dumpEvent(event);
        }
    }

    private static void dumpEvent(Event event) {
        System.out.println("EVENT: " + event);

        int heatNumber = 0;
        for (Heat heat : event.buildHeats(3)) {
            heatNumber++;
            System.out.println(String.format("HEAT #%d (size=%d):", heatNumber, heat.getApplications().size()));
            for (Application application : heat.getApplications()) {
                System.out.println(application);
            }
        }
    }

    private static void xmlDumpEvents(EntityManager em) {
        @SuppressWarnings({"unchecked"}) // JPA
                List<Event> events = (List<Event>) Collections.checkedList(
                em.createQuery("from Event").getResultList(), Event.class);

        Marshaller m = new ContextHolder().createMarshaller();

        for (Event event : events) {
            try {
                m.marshal(event, System.out);
            } catch (JAXBException e) {
                throw new IllegalArgumentException(e);
            }
            break; // TODO: fixme: dumping only the first
        }

    }
}
