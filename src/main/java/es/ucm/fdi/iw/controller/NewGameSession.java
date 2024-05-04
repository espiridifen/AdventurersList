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

import es.ucm.fdi.iw.model.game.Game;

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

}
