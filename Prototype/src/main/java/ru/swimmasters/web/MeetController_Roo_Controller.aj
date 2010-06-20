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
import ru.swimmasters.domain.Meet;
import ru.swimmasters.domain.Pool;

privileged aspect MeetController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String MeetController.create(@Valid Meet meet, BindingResult result, ModelMap modelMap) {
        if (meet == null) throw new IllegalArgumentException("A meet is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("meet", meet);
            addDateTimeFormatPatterns(modelMap);
            return "meets/create";
        }
        meet.persist();
        return "redirect:/meets/" + meet.getId();
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String MeetController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("meet", new Meet());
        addDateTimeFormatPatterns(modelMap);
        List dependencies = new ArrayList();
        if (Pool.countPools() == 0) {
            dependencies.add(new String[]{"pool", "pools"});
        }
        modelMap.addAttribute("dependencies", dependencies);
        return "meets/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String MeetController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        addDateTimeFormatPatterns(modelMap);
        modelMap.addAttribute("meet", Meet.findMeet(id));
        return "meets/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String MeetController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("meets", Meet.findMeetEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Meet.countMeets() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("meets", Meet.findAllMeets());
        }
        addDateTimeFormatPatterns(modelMap);
        return "meets/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String MeetController.update(@Valid Meet meet, BindingResult result, ModelMap modelMap) {
        if (meet == null) throw new IllegalArgumentException("A meet is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("meet", meet);
            addDateTimeFormatPatterns(modelMap);
            return "meets/update";
        }
        meet.merge();
        return "redirect:/meets/" + meet.getId();
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String MeetController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("meet", Meet.findMeet(id));
        addDateTimeFormatPatterns(modelMap);
        return "meets/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String MeetController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Meet.findMeet(id).remove();
        return "redirect:/meets?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @ModelAttribute("pools")
    public Collection<Pool> MeetController.populatePools() {
        return Pool.findAllPools();
    }
    
    Converter<Meet, String> MeetController.getMeetConverter() {
        return new Converter<Meet, String>() {
            public String convert(Meet meet) {
                return new StringBuilder().append(meet.getName()).append(" ").append(meet.getStartDate()).toString();
            }
        };
    }
    
    Converter<Pool, String> MeetController.getPoolConverter() {
        return new Converter<Pool, String>() {
            public String convert(Pool pool) {
                return new StringBuilder().append(pool.getName()).append(" ").append(pool.getLocation()).append(" ").append(pool.getLanesCount()).toString();
            }
        };
    }
    
    @InitBinder
    void MeetController.registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getMeetConverter());
            conversionService.addConverter(getPoolConverter());
        }
    }
    
    void MeetController.addDateTimeFormatPatterns(ModelMap modelMap) {
        modelMap.addAttribute("meet_startdate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
    }
    
}
