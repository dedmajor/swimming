package ru.swimmasters.web;

import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.swimmasters.domain.Application;
import ru.swimmasters.domain.Athlete;
import ru.swimmasters.domain.Event;

privileged aspect ApplicationController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String ApplicationController.create(@Valid Application application, BindingResult result, ModelMap modelMap) {
        if (application == null) throw new IllegalArgumentException("A application is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("application", application);
            return "applications/create";
        }
        application.persist();
        return "redirect:/applications/" + application.getId();
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String ApplicationController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("application", new Application());
        List dependencies = new ArrayList();
        if (Event.countEvents() == 0) {
            dependencies.add(new String[]{"event", "events"});
        }
        if (Athlete.countAthletes() == 0) {
            dependencies.add(new String[]{"participant", "athletes"});
        }
        modelMap.addAttribute("dependencies", dependencies);
        return "applications/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String ApplicationController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("application", Application.findApplication(id));
        return "applications/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String ApplicationController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("applications", Application.findApplicationEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Application.countApplications() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("applications", Application.findAllApplications());
        }
        return "applications/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String ApplicationController.update(@Valid Application application, BindingResult result, ModelMap modelMap) {
        if (application == null) throw new IllegalArgumentException("A application is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("application", application);
            return "applications/update";
        }
        application.merge();
        return "redirect:/applications/" + application.getId();
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String ApplicationController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("application", Application.findApplication(id));
        return "applications/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String ApplicationController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Application.findApplication(id).remove();
        return "redirect:/applications?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @ModelAttribute("athletes")
    public Collection<Athlete> ApplicationController.populateAthletes() {
        return Athlete.findAllAthletes();
    }
    
    @ModelAttribute("events")
    public Collection<Event> ApplicationController.populateEvents() {
        return Event.findAllEvents();
    }
    
    Converter<Application, String> ApplicationController.getApplicationConverter() {
        return new Converter<Application, String>() {
            public String convert(Application application) {
                return new StringBuilder().append(application.getDeclaredTime()).toString();
            }
        };
    }
    
    Converter<Athlete, String> ApplicationController.getAthleteConverter() {
        return new Converter<Athlete, String>() {
            public String convert(Athlete athlete) {
                return new StringBuilder().append(athlete.getName()).append(" ").append(athlete.getBirthYear()).toString();
            }
        };
    }
    
    Converter<Event, String> ApplicationController.getEventConverter() {
        return new Converter<Event, String>() {
            public String convert(Event event) {
                return new StringBuilder().append(event.getHoldingDate()).toString();
            }
        };
    }
    
    @InitBinder
    void ApplicationController.registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getApplicationConverter());
            conversionService.addConverter(getAthleteConverter());
            conversionService.addConverter(getEventConverter());
        }
    }
    
}
