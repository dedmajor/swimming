package ru.swimmasters.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.Athlete;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * User: dedmajor
 * Date: 3/12/11
 */
@Controller
public class EntriesController {
    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/listEntries.html")
    public ModelAndView listEntries() {
        ModelAndView mav = new ModelAndView("listEntries");
        List<Athlete> athletes = (List<Athlete>) entityManager.createQuery("from SwimmastersAthlete")
                .getResultList();
        mav.addObject("athletes", athletes);
        return mav;
    }
}
