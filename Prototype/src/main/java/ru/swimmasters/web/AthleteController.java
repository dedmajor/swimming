package ru.swimmasters.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import ru.swimmasters.domain.Athlete;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "athletes", formBackingObject = Athlete.class)
@RequestMapping("/athletes")
@Controller
public class AthleteController {
}
