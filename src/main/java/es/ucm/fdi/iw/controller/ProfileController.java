package es.ucm.fdi.iw.controller;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
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
        long userId = ((User)httpSession.getAttribute("u")).getId();
        TypedQuery<User> u_query = entityManager.createQuery("select u from User u where u.id = :userId", User.class);
        u_query.setParameter("userId", userId);
        u_query.setMaxResults(1);
        User u;

        try {
            u = u_query.getSingleResult();
        }
        catch (NoResultException e) {
            log.error("Error: " +e);
            return "error";
        }
        catch (Exception e) {
            log.error("Error: " +e);
            return "error";
        }
        
        model.addAttribute("username", u.getUsername());

        // Now add the user's games
        List<Game> games = entityManager.createQuery("select g from Game g where g.owner.id = :userId", Game.class)
                                        .setParameter("userId", userId)
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
