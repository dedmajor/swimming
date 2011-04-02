package ru.swimmasters.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
@Controller
public class EventsController {
    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/listEvents.html")
    public ModelAndView listEntries() {
        ModelAndView mav = new ModelAndView("listEvents");
        mav.addObject("events",  entityManager.createQuery("from SwimMastersEvent").getResultList());
        return mav;
    }
}
