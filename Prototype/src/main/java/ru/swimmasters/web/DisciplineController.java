package ru.swimmasters.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import ru.swimmasters.domain.Discipline;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "disciplines", formBackingObject = Discipline.class)
@RequestMapping("/disciplines")
@Controller
public class DisciplineController {
}
