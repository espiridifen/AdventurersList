package es.ucm.fdi.iw.controller;

import javax.transaction.Transactional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NewGameController {
    
    @GetMapping("/newGame")
    public String newGame(Model model) {
        return "newGame.html";
    }

    @PostMapping("/generateNewGame")
    @Transactional
    public String generateNewGame(Model model) {
        return ":redirect:/game";
    }
}
