package ru.swimmasters.web;

import java.lang.Long;
import java.lang.String;
import java.util.Arrays;
import java.util.Collection;
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
import ru.swimmasters.domain.Discipline;
import ru.swimmasters.domain.Gender;

privileged aspect DisciplineController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String DisciplineController.create(@Valid Discipline discipline, BindingResult result, ModelMap modelMap) {
        if (discipline == null) throw new IllegalArgumentException("A discipline is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("discipline", discipline);
            return "disciplines/create";
        }
        discipline.persist();
        return "redirect:/disciplines/" + discipline.getId();
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String DisciplineController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("discipline", new Discipline());
        return "disciplines/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String DisciplineController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("discipline", Discipline.findDiscipline(id));
        return "disciplines/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String DisciplineController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("disciplines", Discipline.findDisciplineEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Discipline.countDisciplines() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("disciplines", Discipline.findAllDisciplines());
        }
        return "disciplines/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String DisciplineController.update(@Valid Discipline discipline, BindingResult result, ModelMap modelMap) {
        if (discipline == null) throw new IllegalArgumentException("A discipline is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("discipline", discipline);
            return "disciplines/update";
        }
        discipline.merge();
        return "redirect:/disciplines/" + discipline.getId();
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String DisciplineController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("discipline", Discipline.findDiscipline(id));
        return "disciplines/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String DisciplineController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Discipline.findDiscipline(id).remove();
        return "redirect:/disciplines?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @ModelAttribute("genders")
    public Collection<Gender> DisciplineController.populateGenders() {
        return Arrays.asList(Gender.class.getEnumConstants());
    }
    
    Converter<Discipline, String> DisciplineController.getDisciplineConverter() {
        return new Converter<Discipline, String>() {
            public String convert(Discipline discipline) {
                return new StringBuilder().append(discipline.getName()).append(" ").append(discipline.getDistance()).toString();
            }
        };
    }
    
    @InitBinder
    void DisciplineController.registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getDisciplineConverter());
        }
    }
    
}
