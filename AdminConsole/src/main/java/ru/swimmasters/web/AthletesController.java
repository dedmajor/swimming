package ru.swimmasters.web;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.ApprovalStatus;
import ru.swimmasters.domain.Athlete;
import ru.swimmasters.domain.SwimMastersAthlete;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * User: dedmajor
 * Date: 3/12/11
 */
@Controller
public class AthletesController {
    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/listAthletes.html")
    public ModelAndView listEntries() {
        ModelAndView mav = new ModelAndView("listAthletes");
        List<Athlete> athletes = (List<Athlete>) entityManager.createQuery("from SwimMastersAthlete")
                .getResultList();
        mav.addObject("athletes", athletes);
        return mav;
    }


    @Transactional
    @RequestMapping("/approveAthlete.html")
    public ModelAndView approveAthlete(@RequestParam("athlete") Long athleteId) {
        return setAthleteStatus(athleteId, ApprovalStatus.APPROVED);
    }

    @Transactional
    @RequestMapping("/rejectAthlete.html")
    public ModelAndView rejectAthlete(@RequestParam("athlete") Long athleteId) {
        return setAthleteStatus(athleteId, ApprovalStatus.REJECTED);
    }

    private ModelAndView setAthleteStatus(Long athleteId, ApprovalStatus status) {
        SwimMastersAthlete athlete = entityManager.find(SwimMastersAthlete.class, athleteId);
        if (athlete == null) {
            throw new IllegalArgumentException("no athlete " + athleteId);
        }

        athlete.setApprovalStatus(status);

        entityManager.persist(athlete);

        return new ModelAndView("redirect:/listAthletes.html");
    }
}
