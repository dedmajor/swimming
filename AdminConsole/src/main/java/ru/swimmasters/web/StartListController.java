package ru.swimmasters.web;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.SwimMastersEvent;
import ru.swimmasters.service.RankingsBuilder;

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
    private RankingsBuilder rankingsBuilder;

    @RequestMapping("/startList.html")
    public ModelAndView listEntries(@RequestParam("event") Long eventId) {
        SwimMastersEvent event = entityManager.find(SwimMastersEvent.class, eventId);
        if (event == null) {
            throw new IllegalStateException("no such event: " + eventId);
        }
        return new ModelAndView("startList")
                .addObject("event", event);
    }

    @Transactional
    @RequestMapping("/prepareAgeRankings.html")
    public ModelAndView prepareAgeRankings(@RequestParam("event") Long eventId) {
        SwimMastersEvent event = entityManager.find(SwimMastersEvent.class, eventId);
        rankingsBuilder.buildEventAgeRankings(event);
        return new ModelAndView("redirect:/startList.html")
                .addObject("event", event.getId());
    }
}
