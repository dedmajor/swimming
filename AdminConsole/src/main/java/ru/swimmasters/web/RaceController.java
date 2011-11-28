package ru.swimmasters.web;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.ReadableDuration;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.*;
import ru.swimmasters.service.RaceRunner;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.util.List;

/**
 * /meets/bsvc-dubna-2011/events/1-female-1x200-breast/heats/1/start
 * /meets/bsvc-dubna-2011/events/1-female-1x200-breast/heats/1/new-lane-results
 *
 * User: dedmajor
 * Date: 5/15/11
 */
@Controller
public class RaceController {
    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private RaceRunner runner;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Duration.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                if (getValue() == null) {
                    return "0";
                }
                return String.valueOf(((ReadableDuration) getValue()).getMillis());
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                Duration value = new Duration(Long.valueOf(text));
                setValue(value);
            }
        });
    }

    @Transactional
    @RequestMapping("/runRace.html")
    public ModelAndView runRace(@RequestParam("heat") Long heatId) {
        SwimMastersHeat heat = getHeat(heatId);

        runner.startRace(heat);

        ModelAndView mav = new ModelAndView("redirect:/editRace.html");
        mav.addObject("heat", heat.getId());
        return mav;
    }

    @RequestMapping("/editRace.html")
    public ModelAndView editRace(@RequestParam("heat") Long heatId) {
        SwimMastersHeat heat = getHeatInProgress(heatId);

        return new ModelAndView("editRace")
                .addObject("heat", heat)
                .addObject("command", new ManualLaneResults(heat));
    }

    @Transactional
    @RequestMapping(value = "/editRace.html", method = RequestMethod.POST)
    public ModelAndView editRace(@RequestParam("heat") Long heatId,
                                 @Valid @ModelAttribute ManualLaneResults laneResults,
                                 BindingResult result) {
        SwimMastersHeat heat = getHeatInProgress(heatId);

        for (ManualLaneResult laneResult : laneResults.getAll()) {
            System.out.println(laneResult);
        }

        // TODO: validate lane result

        List<Result> registeredResults = runner.registerAllLaneResults(heat, laneResults);

        for (Result registeredResult : registeredResults) {
            entityManager.persist(registeredResult);
        }

        runner.finishRace(heat);

        return new ModelAndView("redirect:/startList.html")
                .addObject("event", heat.getEvent().getId());
    }


    private SwimMastersHeat getHeat(Long heatId) {
        SwimMastersHeat heat = entityManager.find(SwimMastersHeat.class, heatId);
        if (heat == null) {
            throw new IllegalStateException("no such heat: " + heatId);
        }
        return heat;
    }

    private SwimMastersHeat getHeatInProgress(Long heatId) {
        SwimMastersHeat heat = getHeat(heatId);
        if (heat.getRaceStatus() != RaceStatus.IN_PROGRESS) {
            throw new IllegalStateException("heat " + heatId + " is not in progress now");
        }
        return heat;
    }
}
