package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Game;
import es.ucm.fdi.iw.model.GameJoin;
import es.ucm.fdi.iw.model.User;

@Controller
public class NewGameController {
	private static final Logger log = LogManager.getLogger(NewGameController.class);
    
    @Autowired
    EntityManager entityManager;

    @Autowired
    HttpSession httpSession;

    @PostMapping("/createNewGame")
    @Transactional
    public String createNewGame(@RequestParam String name, 
                                @RequestParam String gamesystem,
                                @RequestParam String date,
                                @RequestParam String meeting,
                                @RequestParam String experience,
                                @RequestParam String description) {
        
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

        Game g = new Game(null, name, description, experience, LocalDateTime.parse(date + "T00:00:00"), 
                        gamesystem, 0, u, gamesystem, meeting, null);
        entityManager.persist(g);

        // Join the user to his own game
        entityManager.persist(new GameJoin(u, g));

        return "redirect:/game?questID=" + g.getId().toString();
    }
}
