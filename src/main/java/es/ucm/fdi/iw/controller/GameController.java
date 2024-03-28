package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.text.html.parser.Entity;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Game;

@Controller
public class GameController {
    
    @Autowired
    EntityManager entityManager;

    @GetMapping("/game")
    @Transactional
    public String game(Model model, @RequestParam("questID") long questID){

        TypedQuery<Game> query = entityManager.createQuery("select g from Game g where g.id = :questID", Game.class);
        query.setParameter("questID", questID);
        query.setMaxResults(1);
        

        model.addAttribute("game", query.getSingleResult());

        return "game";
    }
}
