package ru.swimmasters.web;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.Athlete;
import ru.swimmasters.domain.SwimmastersAthlete;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dedmajor
 * Date: 3/12/11
 */
@Controller
public class EntriesController {
    @RequestMapping("/listEntries.html")
    public ModelAndView listEntries() {
        ModelAndView mav = new ModelAndView("listEntries");
        List<Athlete> athletes = new ArrayList<Athlete>();
        SwimmastersAthlete athlete1 = new SwimmastersAthlete();
        athlete1.birthDate = new LocalDate();
        athletes.add(athlete1);
        SwimmastersAthlete athlete2 = new SwimmastersAthlete();
        athlete2.birthDate = new LocalDate("2011-03-06");
        athletes.add(athlete2);
        mav.addObject("athletes", athletes);
        return mav;
    }
}
