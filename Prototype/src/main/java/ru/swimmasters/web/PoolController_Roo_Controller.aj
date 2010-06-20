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
import ru.swimmasters.domain.Pool;

privileged aspect PoolController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String PoolController.create(@Valid Pool pool, BindingResult result, ModelMap modelMap) {
        if (pool == null) throw new IllegalArgumentException("A pool is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("pool", pool);
            return "pools/create";
        }
        pool.persist();
        return "redirect:/pools/" + pool.getId();
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String PoolController.createForm(ModelMap modelMap) {
        modelMap.addAttribute("pool", new Pool());
        return "pools/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String PoolController.show(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("pool", Pool.findPool(id));
        return "pools/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String PoolController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap modelMap) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            modelMap.addAttribute("pools", Pool.findPoolEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Pool.countPools() / sizeNo;
            modelMap.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            modelMap.addAttribute("pools", Pool.findAllPools());
        }
        return "pools/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String PoolController.update(@Valid Pool pool, BindingResult result, ModelMap modelMap) {
        if (pool == null) throw new IllegalArgumentException("A pool is required");
        if (result.hasErrors()) {
            modelMap.addAttribute("pool", pool);
            return "pools/update";
        }
        pool.merge();
        return "redirect:/pools/" + pool.getId();
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String PoolController.updateForm(@PathVariable("id") Long id, ModelMap modelMap) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        modelMap.addAttribute("pool", Pool.findPool(id));
        return "pools/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String PoolController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        if (id == null) throw new IllegalArgumentException("An Identifier is required");
        Pool.findPool(id).remove();
        return "redirect:/pools?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    Converter<Pool, String> PoolController.getPoolConverter() {
        return new Converter<Pool, String>() {
            public String convert(Pool pool) {
                return new StringBuilder().append(pool.getName()).append(" ").append(pool.getLocation()).append(" ").append(pool.getLanesCount()).toString();
            }
        };
    }
    
    @InitBinder
    void PoolController.registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getPoolConverter());
        }
    }
    
}
