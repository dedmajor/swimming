package org.olympic.swimming;

import org.olympic.swimming.domain.Event;
import org.olympic.swimming.domain.Heat;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

/**
 * User: dedmajor
 * Date: May 23, 2010
 */
public class HeatBuilderApp {
    public static void main(String args[]) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        EntityManagerFactoryInfo entityManagerFactory =
                (EntityManagerFactoryInfo) context.getBean("entityManagerFactory");
        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();

        @SuppressWarnings({"unchecked"}) // JPA
        List<Event> events = (List<Event>) Collections.checkedList(
                em.createQuery("from Event").getResultList(), Event.class);

        for (Event event : events) {
            System.out.println("EVENT: " + event);

            int heatNumber = 0;
            for (Heat heat : event.buildHeats(3)) {
                heatNumber++;
                System.out.println("HEAT #" + heatNumber + ": " + heat);
            }
        }
    }
}
