package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.GameJoin;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.game.ExperienceEnum;
import es.ucm.fdi.iw.model.game.Game;
import es.ucm.fdi.iw.model.game.GameSystemEnum;
import es.ucm.fdi.iw.model.gamesession.GameSession;

@Controller
public class NewGameController {

    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private LocalData localData;

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
            @RequestParam String description,
            @RequestParam("photo") MultipartFile photo,
            HttpServletResponse response) {

        long userId = ((User) httpSession.getAttribute("u")).getId();
        User u = entityManager.find(User.class, userId);

        Game g = new Game(null, name, description, ExperienceEnum.valueOf(experience),
                LocalDateTime.parse(date),
                GameSystemEnum.valueOf(gamesystem), 0, u, gamesystem, meeting, null, null);
        entityManager.persist(g);

        // Join the user to his own game
        entityManager.persist(new GameJoin(u, g));

        log.info("Updating photo for game {}", g.getId());
        File f = localData.getFile("game", "" + g.getId() + ".png");
        if (photo.isEmpty()) {
            log.info("failed to upload photo: empty file?");
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
                log.info("Uploaded photo for game {} into {}!", g.getId(), f.getAbsolutePath());
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                log.warn("Error uploading " + g.getId() + " ", e);
            }
        }

        return "redirect:/game?questID=" + g.getId().toString();
    }
    
    @GetMapping("/editGame")
    @Transactional
    public String getModifySession(Model model, @RequestParam long gameId) {
        User u = (User) httpSession.getAttribute("u");
        Game g = entityManager.find(Game.class, gameId);
        long ownerId = g.getOwner().getId();

        if (ownerId != u.getId()) return "error";

        model.addAttribute("game", g);

        return "editgame.html";
    }

    @PostMapping("/editGame")
    @Transactional
    public String postModifySession(@RequestParam Long id,
                                    @RequestParam String name,
                                    @RequestParam String gamesystem,
                                    @RequestParam String date,
                                    @RequestParam String meeting,
                                    @RequestParam String experience,
                                    @RequestParam String description,
                                    @RequestParam("photo") MultipartFile photo,
                                    HttpServletResponse response) {
        User u = (User) httpSession.getAttribute("u");
        Game g = entityManager.find(Game.class, id);
        long ownerId = g.getOwner().getId();

        if (ownerId != u.getId()) return "error";

        g.setName(name);
        g.setGamesystem(GameSystemEnum.valueOf(gamesystem));
        g.setDate(LocalDateTime.parse(date));
        g.setMeeting(meeting);
        g.setExperience(ExperienceEnum.valueOf(experience));
        g.setDescription(description);

        log.info("Updating photo for game {}", g.getId());
        File f = localData.getFile("game", "" + g.getId() + ".png");
        if (photo.isEmpty()) {
            log.info("Photo not updated for game {}", g.getId());
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
                log.info("Uploaded photo for game {} into {}!", g.getId(), f.getAbsolutePath());
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                log.warn("Error uploading " + g.getId() + " ", e);
            }
        }
        

        return "redirect:/game?questID=" + g.getId().toString();
    }
}
