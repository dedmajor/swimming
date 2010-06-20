package ru.swimmasters.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import ru.swimmasters.domain.Pool;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "pools", formBackingObject = Pool.class)
@RequestMapping("/pools")
@Controller
public class PoolController {
}
