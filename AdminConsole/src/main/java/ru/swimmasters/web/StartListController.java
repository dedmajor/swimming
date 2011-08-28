package ru.swimmasters.web;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.*;
import ru.swimmasters.service.RankingsBuilder;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

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

        clearAgeRankings(event);
        entityManager.flush();

        AgeRankings rankings = rankingsBuilder.buildEventAgeRankings(event);
        persistAgeRankings(rankings);

        return new ModelAndView("redirect:/startList.html")
                .addObject("event", event.getId());
    }

    private void persistAgeRankings(AgeRankings ageRankings) {
        for (AgeRanking ranking : ageRankings.getAll()) {
            entityManager.persist(ranking);
            persistGroupRankings(ranking.getGroupRankings());
        }
    }

    private void persistGroupRankings(GroupRankings groupRankings) {
        for (GroupRanking groupRanking : groupRankings.getAll()) {
            entityManager.persist(groupRanking);
        }
    }

    private void clearAgeRankings(SwimMastersEvent event) {
        for (AgeRanking ageRanking : event.getAgeRankings().getAll()) {
            clearGroupRankings(ageRanking);
            entityManager.remove(ageRanking);
        }
        event.setAgeRankings(Collections.<SwimMastersAgeRanking>emptyList());
    }

    private void clearGroupRankings(AgeRanking ageRanking) {
        for (GroupRanking groupRanking : ageRanking.getGroupRankings().getAll()) {
            entityManager.remove(groupRanking);
        }
        ((SwimMastersAgeRanking) ageRanking).setGroupRankings(Collections.<SwimMastersGroupRanking>emptyList());
    }
}
