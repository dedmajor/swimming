package ru.swimmasters.web;

import org.joda.time.Duration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.swimmasters.domain.*;
import ru.swimmasters.service.RaceRunner;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * User: dedmajor
 * Date: 5/15/11
 */
@Controller
public class RaceController {
    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private RaceRunner runner;

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

        List<ManualLaneResult> results = new ArrayList<ManualLaneResult>();
        results.add(new ManualLaneResult(1, new Duration(1000)));
        results.add(new ManualLaneResult(2, new Duration(1000)));
        results.add(new ManualLaneResult(3, new Duration(1000)));
        LaneResults result = new ManualLaneResults(results);

        ModelAndView mav = new ModelAndView("editRace");
        mav.addObject("heat", heat);
        mav.addObject("command", result);
        return mav;
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

        System.out.println();

        runner.registerAllLaneResults(heat, laneResults);

        ModelAndView mav = new ModelAndView("redirect:/editRace.html");
        mav.addObject("heat", heat.getId());
        return mav;
    }

    @Transactional
    @RequestMapping("/finishRace.html")
    public ModelAndView stopRace(@RequestParam("heat") Long heatId) {
        SwimMastersHeat heat = getHeat(heatId);

        runner.finishRace(heat);

        ModelAndView mav = new ModelAndView("redirect:/startList.html");
        mav.addObject("event", heat.getEvent());
        return mav;
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
