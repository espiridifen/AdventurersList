package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Game;
import es.ucm.fdi.iw.model.Quest;

import java.util.ArrayList;
import java.util.List;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	// private static final Logger log = LogManager.getLogger(RootController.class);

	@GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @Autowired
    EntityManager entityManager;
    
    
	@GetMapping("/")
    @Transactional
    public String index(Model model) {
        List<Game> games = entityManager.createQuery("select g from Game g", Game.class).getResultList();

        List<Quest> quests = new ArrayList<>(); // Initialize the quests variable

        for (Game game : games) {
            Quest q = new Quest();
            q.setName(game.getName());
            q.setSystem(game.getGamesystem());
            q.setDate(game.getDate().toString());
            q.setId(game.getId());
            quests.add(q);
        }

        model.addAttribute("availableQuests", quests);

        return "landingpage";
    }


    @PostMapping("/quest")
    @Transactional
    public String getGamePage(Model model, @RequestParam("questID") Long questID) {

        TypedQuery<Game> query = entityManager.createQuery("select g from Game g where g.id = :questID", Game.class);
        query.setParameter("questID", questID);
        query.setMaxResults(1);

        model.addAttribute("game", query.getSingleResult());

        return "redirect:/game?questID=" + questID;
    }

    @GetMapping("/newGame")
    public String getGamePage(Model model) {
        return "newgame";
    }
}
