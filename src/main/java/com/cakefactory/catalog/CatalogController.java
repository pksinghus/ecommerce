package com.cakefactory.catalog;

import java.util.Map;

import com.cakefactory.basket.Basket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
class CatalogController {

    private final CatalogService catalogService;
    private final Basket basket;

    CatalogController(CatalogService catalogService, Basket basket) {
        this.catalogService = catalogService;
        this.basket = basket;
    }

    @GetMapping("/")
    ModelAndView index() {
        return new ModelAndView("catalog", Map.of("items", this.catalogService.getItems(), "basketTotal", this.basket.getTotalItems()));
    }

}