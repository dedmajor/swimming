package org.olympic.swimming;

import org.olympic.swimming.domain.Event;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import javax.persistence.EntityManager;
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

        // TODO: FIXME: unchecked cast
        List<Event> events = (List<Event>) em.createQuery("from Event").getResultList();

        for (Event event : events) {
            System.out.println("EVENT: " + event);
            System.out.println("APPLICATIONS: " + event.getApplications());
        }
    }
}
