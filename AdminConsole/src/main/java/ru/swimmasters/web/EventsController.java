package ru.swimmasters.web;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.*;
import ru.swimmasters.service.StartListBuilder;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
@Controller
public class EventsController {
    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private StartListBuilder builder;

    @RequestMapping("/listEvents.html")
    public ModelAndView listEntries() {
        ModelAndView mav = new ModelAndView("listEvents");
        // TODO: FIXME: meet.getEvents()
        mav.addObject("events",
                entityManager.createQuery("from SwimMastersEvent order by number")
                        .getResultList());
        return mav;
    }

    @Transactional
    @RequestMapping("/prepareAllStartLists.html")
    public ModelAndView prepareAllStartLists() {
        // TODO: FIXME: move to the StartListBuilder service when Meet object is ready
        @SuppressWarnings({"unchecked"}) List<SwimMastersEvent> events =
                entityManager.createQuery("from SwimMastersEvent order by number")
                .getResultList();
        for (SwimMastersEvent event : events) {
            if (event.getStartListEntries().getAll().isEmpty()) {
                continue;
            }
            // TODO: FIXME: non-transient
            event.setDate(new LocalDate("2011-05-20"));
            // TODO: FIXME: non-transient
            event.setAgeGroups(SwimMastersAgeGroups.createDefaultGroups());
            SwimMastersPool pool = new SwimMastersPool();
            event.setPool(pool);
            pool.setLaneMin(2);
            pool.setLaneMax(8);

            List<Heat> cleanupHeats = builder.buildHeats(event);

            for (Heat heat : cleanupHeats) {
                entityManager.remove(heat);
            }

            for (Entry entry : event.getStartListEntries().getAll()) {
                entityManager.persist(entry.getHeat());
                entityManager.persist(entry);
            }

            entityManager.persist(event);
        }
        return new ModelAndView("redirect:/listEvents.html");
    }
}
