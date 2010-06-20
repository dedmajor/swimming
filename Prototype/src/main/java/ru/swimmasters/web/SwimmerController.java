package ru.swimmasters.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import ru.swimmasters.domain.Swimmer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "swimmers", formBackingObject = Swimmer.class)
@RequestMapping("/swimmers")
@Controller
public class SwimmerController {
}
