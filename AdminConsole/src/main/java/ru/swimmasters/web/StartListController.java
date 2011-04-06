package ru.swimmasters.web;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.Entry;
import ru.swimmasters.domain.SwimMastersAgeGroups;
import ru.swimmasters.domain.SwimMastersEvent;
import ru.swimmasters.domain.SwimMastersPool;
import ru.swimmasters.service.StartListBuilder;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
@Controller
public class StartListController {
    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private StartListBuilder builder;

    @Transactional
    @RequestMapping("/startList.html")
    public ModelAndView listEntries(@RequestParam("event") Long eventId) {
        ModelAndView mav = new ModelAndView("startList");
        SwimMastersEvent event = entityManager.find(SwimMastersEvent.class, eventId);
        if (event == null) {
            throw new IllegalStateException("no such event: " + eventId);
        }
        event.setDate(new LocalDate("2010-11-05"));
        event.setAgeGroups(SwimMastersAgeGroups.createDefaultGroups());
        SwimMastersPool pool = new SwimMastersPool();
        event.setPool(pool);
        pool.setLaneMin(2);
        pool.setLaneMax(8);
        builder.buildHeats(event);

        // TODO: remove old heats
        for (Entry entry : event.getEntries().getAll()) {
            entityManager.persist(entry.getHeat());
        }

        mav.addObject("event", event);
        return mav;
    }
}
