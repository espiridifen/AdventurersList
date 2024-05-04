package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.parser.Entity;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.GameJoin;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.game.ExperienceEnum;
import es.ucm.fdi.iw.model.game.Game;
import es.ucm.fdi.iw.model.game.GameSystemEnum;
import es.ucm.fdi.iw.model.gamesession.GameSessionService;

@Controller
public class NewGameSession {
    
    @Autowired
    EntityManager entityManager;

    @GetMapping("/newgamesession")
    @Transactional
    public String newGameSession(Model model, @RequestParam("gameId") Long gameId){
        model.addAttribute("gameId", gameId);   


        TypedQuery<Game> query = entityManager.createQuery("select g from Game g where g.id = :gameId", Game.class)
                    .setParameter("gameId", gameId)
                    .setMaxResults(1);
        
        model.addAttribute("game", query.getSingleResult());

        return "newgamesession.html";
    }

    @Autowired
    HttpSession httpSession;

    @Autowired
    private GameSessionService gameSessionService;

    @PostMapping("/createNewGameSession")
    @Transactional
    public String createNewGame(@RequestParam long gameId, 
                                @RequestParam String title,
                                @RequestParam String date,
                                @RequestParam String location) {
        
        long userId = ((User)httpSession.getAttribute("u")).getId();
        User u = entityManager.find(User.class, userId);
        TypedQuery<Game> g_query = entityManager.createQuery("select g from Game g where g.id = :gameId", Game.class);
        g_query.setParameter("gameId", gameId);
        g_query.setMaxResults(1);
        Game g;

        try {
            g = g_query.getSingleResult();
        }
        catch (NoResultException e) {
            return "error";
        }
        catch (Exception e) {
            return "error";
        }

        if (g.getOwner().getId() != userId) { // If the user calling this is not the owner -> NOT AUTHORIZED
            return "error/403";
        }

        gameSessionService.createGameSession(g, title, LocalDateTime.parse(date + "T00:00:00"), location);

        return "redirect:/game?questID=" + g.getId().toString();
    }

}
