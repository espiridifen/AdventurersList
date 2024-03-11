package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ucm.fdi.iw.model.Game;
import es.ucm.fdi.iw.model.Lorem;
import es.ucm.fdi.iw.model.Quest;
import es.ucm.fdi.iw.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    EntityManager entityManager;
    
    

	@GetMapping("/")
    public String index(Model model) {

        Game gamePrueba = new Game(new User());
        gamePrueba.setName("Marberto");
        gamePrueba.setGamesystem("Dungeons and Dragons 5th Edition");
        gamePrueba.setDate(LocalDateTime.of(2017, 3, 7, 16, 00, 00));
        gamePrueba.setExperience("Beginner");
        gamePrueba.setSessionQuantity(1);
        gamePrueba.setType("One-shot");
        gamePrueba.setMeeting("Online");

        //entityManager.persist(gamePrueba);
        
        List<Game> games = entityManager.createQuery("select g from Game g", Game.class).getResultList();

        List<Quest> quests = new ArrayList<>(); // Initialize the quests variable

        games.add(gamePrueba); //Para ver si Thymeleaf tira

        for (Game game : games) {
            Quest q = new Quest();
            q.setName(game.getName());
            q.setSystem(game.getGamesystem());
            q.setDate(game.getDate().toString());
            quests.add(q);
        }

        model.addAttribute("availableQuests", quests);

        // model.addAttribute("availableQuests", Lorem.nombreAlAzar());

        // Quest game = new Quest();
        // game.setName("Marberto");
        // game.setSystem("Dungeons and Dragons 5th Edition");
        // game.setDate("March 7th, 2021");

        // model.addAttribute("game", game);




        return "landingpage";
    }
}
