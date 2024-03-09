package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ucm.fdi.iw.model.Lorem;
import es.ucm.fdi.iw.model.Quest;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);

	// @GetMapping("/login")
    // public String login(Model model) {
    //     return "login";
    // }

	@GetMapping("/")
    public String index(Model model) {



        // model.addAttribute("availableQuests", Lorem.nombreAlAzar());

        // Quest game = new Quest();
        // game.setName("Marberto");
        // game.setSystem("Dungeons and Dragons 5th Edition");
        // game.setDate("March 7th, 2021");

        // model.addAttribute("game", game);




        return "landingpage";
    }
}
