package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {
    
    @GetMapping("/busqueda")
    public String busqueda(Model model) {
        return "busqueda";
    }
}
