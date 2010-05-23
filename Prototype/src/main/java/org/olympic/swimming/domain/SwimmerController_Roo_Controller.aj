package org.olympic.swimming.domain;

import java.lang.Long;
import java.lang.String;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.olympic.swimming.domain.Swimmer;
import org.springframework.context.i18n.LocaleContextHolder;
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

privileged aspect SwimmerController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String SwimmerController.create(@Valid Swimmer swimmer, BindingResult result, ModelMap modelMap) {
        if (swimmer == null) throw new IllegalArgumentException("A swimmer is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("swimmer", swimmer);
            addDateTimeFormatPatterns(modelMap);
            return "swimmers/create";
        }
        swimmer.persist();
        return "redirect:/swimmers/" + swimmer.getId();
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String SwimmerController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("swimmer", new Swimmer());
        addDateTimeFormatPatterns(modelMap);
        return "swimmers/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String SwimmerController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        addDateTimeFormatPatterns(modelMap);
        modelMap.addAttribute("swimmer", Swimmer.findSwimmer(id));
        return "swimmers/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String SwimmerController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("swimmers", Swimmer.findSwimmerEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Swimmer.countSwimmers() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("swimmers", Swimmer.findAllSwimmers());
        }
        addDateTimeFormatPatterns(modelMap);
        return "swimmers/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String SwimmerController.update(@Valid Swimmer swimmer, BindingResult result, ModelMap modelMap) {
        if (swimmer == null) throw new IllegalArgumentException("A swimmer is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("swimmer", swimmer);
            addDateTimeFormatPatterns(modelMap);
            return "swimmers/update";
        }
        swimmer.merge();
        return "redirect:/swimmers/" + swimmer.getId();
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String SwimmerController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("swimmer", Swimmer.findSwimmer(id));
        addDateTimeFormatPatterns(modelMap);
        return "swimmers/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String SwimmerController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Swimmer.findSwimmer(id).remove();
        return "redirect:/swimmers?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    Converter<Swimmer, String> SwimmerController.getSwimmerConverter() {
        return new Converter<Swimmer, String>() {
            public String convert(Swimmer swimmer) {
                return new StringBuilder().append(swimmer.getName()).append(" ").append(swimmer.getBirthDate()).toString();
            }
        };
    }
    
    @InitBinder
    void SwimmerController.registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getSwimmerConverter());
        }
    }
    
    void SwimmerController.addDateTimeFormatPatterns(ModelMap modelMap) {
        modelMap.addAttribute("swimmer_birthdate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
    }
    
}
