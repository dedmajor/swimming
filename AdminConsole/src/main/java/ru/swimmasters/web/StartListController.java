package ru.swimmasters.web;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.SwimMastersAgeGroups;
import ru.swimmasters.domain.SwimMastersEvent;

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

    @RequestMapping("/startList.html")
    public ModelAndView listEntries(@RequestParam("event") Long eventId) {
        ModelAndView mav = new ModelAndView("startList");
        SwimMastersEvent event = entityManager.find(SwimMastersEvent.class, eventId);
        if (event == null) {
            throw new IllegalStateException("no such event: " + eventId);
        }
        // TODO: FIXME: non-transient
        event.setDate(new LocalDate("2011-05-20"));
        // TODO: FIXME: non-transient
        event.setAgeGroups(SwimMastersAgeGroups.createDefaultGroups());

        mav.addObject("event", event);
        return mav;
    }
}
