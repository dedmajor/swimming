package org.olympic.swimming.domain;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.olympic.swimming.domain.Pool;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "pools", formBackingObject = Pool.class)
@RequestMapping("/pools")
@Controller
public class PoolController {
}
