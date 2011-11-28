package ru.swimmasters.web;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.*;
import ru.swimmasters.service.MandateCommittee;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *  /meets/bsvc-dubna-2011/athletes/
 *  /meets/bsvc-dubna-2011/athletes/1024/approve
 *  /meets/bsvc-dubna-2011/athletes/1024/reject
 *  /meets/bsvc-dubna-2011/approve-all-athletes
 *
 * User: dedmajor
 * Date: 3/12/11
 */
@Controller
public class AthletesController {
    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private MandateCommittee mandateCommittee;

    @RequestMapping("/listAthletes.html")
    public ModelAndView listEntries(@RequestParam("meet") String meetId) {
        SwimMastersMeet meet = entityManager.find(SwimMastersMeet.class, meetId);
        return new ModelAndView("listAthletes").addObject("meet", meet);
    }

    @Transactional
    @RequestMapping("/approveAllAthletes.html")
    public ModelAndView approveAllAthletes(@RequestParam("meet") String meetId) {
        SwimMastersMeet meet = entityManager.find(SwimMastersMeet.class, meetId);
        for (MeetAthlete athlete : meet.getMeetAthletes().getAll()) {
            if (athlete.getApprovalStatus() == ApprovalStatus.PENDING) {
                mandateCommittee.approveAthlete(athlete);
                entityManager.persist(athlete);
            }
        }
        return new ModelAndView("redirect:/listAthletes.html")
                .addObject("meet", meet.getId());
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
        SwimMastersMeetAthlete athlete = entityManager.find(SwimMastersMeetAthlete.class, athleteId);
        if (athlete == null) {
            throw new IllegalArgumentException("no athlete " + athleteId);
        }

        mandateCommittee.setAthleteStatus(athlete, status);

        entityManager.persist(athlete);

        return new ModelAndView("redirect:/listAthletes.html")
                .addObject("meet", athlete.getMeet().getId());
    }
}
