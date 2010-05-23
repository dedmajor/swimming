package org.olympic.swimming.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.olympic.swimming.domain.Swimmer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "swimmers", formBackingObject = Swimmer.class)
@RequestMapping("/swimmers")
@Controller
public class SwimmerController {
}
