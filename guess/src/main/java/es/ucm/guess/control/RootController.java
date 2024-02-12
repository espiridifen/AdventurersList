package es.ucm.guess.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


import jakarta.servlet.http.HttpSession;

@Controller 
public class RootController {
    @GetMapping("/")
public String index(HttpSession session, Model model, @RequestParam(required = false) Integer entero) { return "index"; }
}
