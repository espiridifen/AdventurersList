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

@Controller
public class NewReportController {
    @Autowired
    EntityManager entityManager;

    @GetMapping("/newreport")
    public String newReport(Model model, @RequestParam("questID") Long questID) {
        TypedQuery<Game> gameSearchQuery = entityManager.createQuery("select g from Game g where g.id = :questID", Game.class);
        gameSearchQuery.setParameter("questID", questID);
        gameSearchQuery.setMaxResults(1);

        model.addAttribute("game", gameSearchQuery.getSingleResult());

        return "newreport.html";
    }

    @PostMapping("/createNewReport")
    @Transactional
    public String createNewReport(Model model) {
        return ":redirect:/";
    }
}
