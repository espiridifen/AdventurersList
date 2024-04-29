package es.ucm.fdi.iw.controller;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReportSentController {
    


    @GetMapping("/reportsent")
    public String reportSent(Model model) {
        return "reportsent";
    }

    @PostMapping("/leave")
    public String leave() {
        return "landingpage";
    }
}
