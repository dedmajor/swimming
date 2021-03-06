package ru.swimmasters.web;

import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
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
import ru.swimmasters.domain.Discipline;
import ru.swimmasters.domain.Event;
import ru.swimmasters.domain.Meet;

privileged aspect EventController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String EventController.create(@Valid Event event, BindingResult result, ModelMap modelMap) {
        if (event == null) throw new IllegalArgumentException("A event is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("event", event);
            addDateTimeFormatPatterns(modelMap);
            return "events/create";
        }
        event.persist();
        return "redirect:/events/" + event.getId();
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String EventController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("event", new Event());
        addDateTimeFormatPatterns(modelMap);
        List dependencies = new ArrayList();
        if (Discipline.countDisciplines() == 0) {
            dependencies.add(new String[]{"discipline", "disciplines"});
        }
        if (Meet.countMeets() == 0) {
            dependencies.add(new String[]{"meet", "meets"});
        }
        modelMap.addAttribute("dependencies", dependencies);
        return "events/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String EventController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        addDateTimeFormatPatterns(modelMap);
        modelMap.addAttribute("event", Event.findEvent(id));
        return "events/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String EventController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("events", Event.findEventEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Event.countEvents() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("events", Event.findAllEvents());
        }
        addDateTimeFormatPatterns(modelMap);
        return "events/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String EventController.update(@Valid Event event, BindingResult result, ModelMap modelMap) {
        if (event == null) throw new IllegalArgumentException("A event is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("event", event);
            addDateTimeFormatPatterns(modelMap);
            return "events/update";
        }
        event.merge();
        return "redirect:/events/" + event.getId();
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String EventController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("event", Event.findEvent(id));
        addDateTimeFormatPatterns(modelMap);
        return "events/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String EventController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Event.findEvent(id).remove();
        return "redirect:/events?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @ModelAttribute("disciplines")
    public Collection<Discipline> EventController.populateDisciplines() {
        return Discipline.findAllDisciplines();
    }
    
    @ModelAttribute("meets")
    public Collection<Meet> EventController.populateMeets() {
        return Meet.findAllMeets();
    }
    
    Converter<Discipline, String> EventController.getDisciplineConverter() {
        return new Converter<Discipline, String>() {
            public String convert(Discipline discipline) {
                return new StringBuilder().append(discipline.getName()).append(" ").append(discipline.getDistance()).toString();
            }
        };
    }
    
    Converter<Event, String> EventController.getEventConverter() {
        return new Converter<Event, String>() {
            public String convert(Event event) {
                return new StringBuilder().append(event.getHoldingDate()).toString();
            }
        };
    }
    
    Converter<Meet, String> EventController.getMeetConverter() {
        return new Converter<Meet, String>() {
            public String convert(Meet meet) {
                return new StringBuilder().append(meet.getName()).append(" ").append(meet.getStartDate()).toString();
            }
        };
    }
    
    @InitBinder
    void EventController.registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getDisciplineConverter());
            conversionService.addConverter(getEventConverter());
            conversionService.addConverter(getMeetConverter());
        }
    }
    
    void EventController.addDateTimeFormatPatterns(ModelMap modelMap) {
        modelMap.addAttribute("event_holdingdate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
    }
    
}
