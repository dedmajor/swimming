package ru.swimmasters.web;

import java.lang.Long;
import java.lang.String;
import javax.validation.Valid;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.swimmasters.domain.Athlete;

privileged aspect AthleteController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String AthleteController.create(@Valid Athlete athlete, BindingResult result, ModelMap modelMap) {
        if (athlete == null) throw new IllegalArgumentException("A athlete is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("athlete", athlete);
            return "athletes/create";
        }
        athlete.persist();
        return "redirect:/athletes/" + athlete.getId();
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String AthleteController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("athlete", new Athlete());
        return "athletes/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String AthleteController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("athlete", Athlete.findAthlete(id));
        return "athletes/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String AthleteController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("athletes", Athlete.findAthleteEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Athlete.countAthletes() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("athletes", Athlete.findAllAthletes());
        }
        return "athletes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String AthleteController.update(@Valid Athlete athlete, BindingResult result, ModelMap modelMap) {
        if (athlete == null) throw new IllegalArgumentException("A athlete is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("athlete", athlete);
            return "athletes/update";
        }
        athlete.merge();
        return "redirect:/athletes/" + athlete.getId();
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String AthleteController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("athlete", Athlete.findAthlete(id));
        return "athletes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String AthleteController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Athlete.findAthlete(id).remove();
        return "redirect:/athletes?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    Converter<Athlete, String> AthleteController.getAthleteConverter() {
        return new Converter<Athlete, String>() {
            public String convert(Athlete athlete) {
                return new StringBuilder().append(athlete.getName()).append(" ").append(athlete.getBirthYear()).toString();
            }
        };
    }
    
    @InitBinder
    void AthleteController.registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getAthleteConverter());
        }
    }
    
}
