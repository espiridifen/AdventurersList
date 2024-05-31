package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.game.Game;
import es.ucm.fdi.iw.model.gamesession.GameSession;
import es.ucm.fdi.iw.model.gamesession.GameSessionService;
import es.ucm.fdi.iw.model.sessionattendance.AttendanceResponseEnum;

@Controller
public class GameSessionController {

    @Autowired
    EntityManager entityManager;

    @Autowired
    HttpSession httpSession;

    @Autowired
    private GameSessionService gameSessionService;

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

    @PostMapping("/set_attendance")
    @Transactional
    public String setAttendanceToSession(@RequestParam long gameId,
                                            @RequestParam Long game_session_id,
                                            @RequestParam boolean attendance_value) {

        User u = (User) httpSession.getAttribute("u");
        Game g = entityManager.find(Game.class, gameId);

        AttendanceResponseEnum att = attendance_value ? AttendanceResponseEnum.CONFIRMED : AttendanceResponseEnum.DENIED;

        if (g != null) {
        // Update the PENDING value to CONFIRMED
        entityManager.createQuery("UPDATE SessionAttendance sa SET sa.response = :confirmedStatus WHERE sa.gameSession.id = :gameSessionId AND sa.user.id = :userId")
                    .setParameter("confirmedStatus", att)
                    .setParameter("gameSessionId", game_session_id)
                    .setParameter("userId", u.getId())
                    .executeUpdate();
        }

        return "redirect:/game?questID=" + g.getId().toString();
    }

    @GetMapping("/editSession")
    @Transactional
    public String getModifySession(Model model, @RequestParam long sessionId) {

        User u = (User) httpSession.getAttribute("u");
        GameSession gs = entityManager.find(GameSession.class, sessionId);
        long ownerId = gs.getGame().getOwner().getId();

        if (ownerId != u.getId()) return "error";

        model.addAttribute("session", gs);

        return "editsession.html";
    }

    @PostMapping("/editSession")
    @Transactional
    public String postModifySession(@RequestParam Long id,
                                @RequestParam String title,
                                @RequestParam String date,
                                @RequestParam String location,
                                @RequestParam String linkToGame) {

        User u = (User) httpSession.getAttribute("u");
        GameSession gs = entityManager.find(GameSession.class, id);
        long ownerId = gs.getGame().getOwner().getId();

        if (ownerId != u.getId()) return "error";

        LocalDateTime parsedDate;
        try {
            parsedDate = LocalDateTime.parse(date);
        } catch (DateTimeParseException e) {
            return "error";
        }

        // Update the GameSession entity
        gs.setTitle(title);
        gs.setDate(parsedDate);
        gs.setLocation(location);
        gs.setLinkToGame(linkToGame);

        return "redirect:/game?questID=" + gs.getGame().getId().toString();
    }
}
