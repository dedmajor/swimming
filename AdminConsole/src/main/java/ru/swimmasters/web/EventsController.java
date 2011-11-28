package ru.swimmasters.web;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.*;
import ru.swimmasters.service.StartListBuilder;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * /meets/bsvc-dubna-2011/events/
 * /meets/bsvc-dubna-2011/new-event-heats
 *
 * User: dedmajor
 * Date: 4/2/11
 */
@Controller
public class EventsController {
    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private StartListBuilder builder;

    // TODO: FIXME: @RequestMapping("/{meet}/listEvents.html")
    @RequestMapping("/listEvents.html")
    public ModelAndView listEntries(@RequestParam("meet") String meetId) {
        SwimMastersMeet meet = entityManager.find(SwimMastersMeet.class, meetId);
        return new ModelAndView("listEvents")
                .addObject("meet", meet);
    }

    @Transactional
    @RequestMapping("/prepareAllStartLists.html")
    public ModelAndView prepareAllStartLists(@RequestParam("meet") String meetId) {
        SwimMastersMeet meet = entityManager.find(SwimMastersMeet.class, meetId);

        List<Heat> cleanupHeats = builder.prepareHeats(meet);

        for (Heat heat : cleanupHeats) {
            entityManager.remove(heat);
        }
        for (Event event : meet.getEvents().getAll()) {
            for (Entry entry : event.getStartListEntries().getAllSortedByAthleteName()) {
                entityManager.persist(entry.getHeat());
                entityManager.persist(entry);
            }
            entityManager.persist(event);
        }
        return new ModelAndView("redirect:/listEvents.html")
                .addObject("meet", meet.getId());
    }
}
