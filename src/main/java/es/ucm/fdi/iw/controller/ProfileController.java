package es.ucm.fdi.iw.controller;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ucm.fdi.iw.model.Game;
import es.ucm.fdi.iw.model.Quest;
import es.ucm.fdi.iw.model.User;

@Controller
public class ProfileController {
	private static final Logger log = LogManager.getLogger(ProfileController.class);
    
    @Autowired
    EntityManager entityManager;

    @Autowired
    HttpSession httpSession;
    
    @GetMapping("/perfil")
    public String perfil(Model model) {
        User u = (User)httpSession.getAttribute("u");
        
        model.addAttribute("username", u.getUsername());

        // Now add the user's games
        List<Game> games = entityManager.createQuery("select g from Game g where g.owner.id = :userId", Game.class)
                                        .setParameter("userId", u.getId())
                                        .getResultList();
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


        return "perfil";
    }
}
