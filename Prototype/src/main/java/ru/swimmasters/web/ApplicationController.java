package ru.swimmasters.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import ru.swimmasters.domain.Application;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "applications", formBackingObject = Application.class)
@RequestMapping("/applications")
@Controller
public class ApplicationController {
}
