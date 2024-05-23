package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Report;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.game.Game;

@Controller
public class NewReportController {
    @Autowired
    EntityManager entityManager;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/newGameReport")
    public String newReport(Model model, @RequestParam("questID") Long questID) {
        TypedQuery<Game> gameSearchQuery = entityManager.createQuery("select g from Game g where g.id = :questID", Game.class);
        gameSearchQuery.setParameter("questID", questID);
        gameSearchQuery.setMaxResults(1);

        model.addAttribute("game", gameSearchQuery.getSingleResult());

        return "newreport.html";
    }

    @PostMapping("/createNewGameReport")
    @Transactional
    public String createNewGameReport(@RequestParam Long questID, @RequestParam String text) {
        Game g = entityManager.find(Game.class, questID);

        if (g == null) {
            return "error";
        }
        
        User u = (User)httpSession.getAttribute("u");

        Report r = new Report(null, u, null, g, null, text);
        entityManager.persist(r);

        return "reportsent.html";
    }

}
