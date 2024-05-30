package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.game.Game;
import es.ucm.fdi.iw.model.gamesession.GameSessionService;

@Controller
public class NewGameSession {

    @Autowired
    EntityManager entityManager;

    @GetMapping("/newgamesession")
    @Transactional
    public String newGameSession(Model model, @RequestParam("gameId") Long gameId) {
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
            @RequestParam String location,
            @RequestParam String linkToGame) {

        long userId = ((User) httpSession.getAttribute("u")).getId();
        Game g = entityManager.find(Game.class, gameId);

        if (g.getOwner().getId() != userId) { // If the user calling this is not the owner -> NOT AUTHORIZED
            return "error/403";
        }

        if (linkToGame == "")
            linkToGame = null;

        gameSessionService.createGameSession(g, title, LocalDateTime.parse(date + "T00:00:00"), location, linkToGame);

        return "redirect:/game?questID=" + g.getId().toString();
    }

}
