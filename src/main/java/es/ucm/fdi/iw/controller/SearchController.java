package es.ucm.fdi.iw.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.game.Game;

@Controller
@RequestMapping("/busqueda")
public class SearchController {
    
    @Autowired
    EntityManager entityManager;

    @GetMapping("")
    public String busqueda(Model model) {
        return "busqueda";
    }
    
    @GetMapping("/gamequery")
    @Transactional
    @ResponseBody
    public List<Game.Transfer> search(@RequestParam("searchQuery") String query) {
        // Create a JPQL query to retrieve games containing the query string in their title (case insensitive)
        String jpql = "SELECT g FROM Game g WHERE LOWER(g.name) LIKE LOWER(:query)";
        List<Game> filteredGames = entityManager.createQuery(jpql, Game.class)
                .setParameter("query", "%" + query.toLowerCase() + "%")
                .getResultList();

        // Convert filtered games to transfer objects
        List<Game.Transfer> result = filteredGames.stream()
                .map(Game.Transfer::new) // Convert Game to Transfer
                .collect(Collectors.toList());

        return result;
    }

    @PostMapping("/quest")
    @Transactional
    public String getGamePage(Model model, @RequestParam("questID") Long questID) {

        TypedQuery<Game> query = entityManager.createQuery("select g from Game g where g.id = :questID", Game.class);
        query.setParameter("questID", questID);
        query.setMaxResults(1);

        model.addAttribute("game", query.getSingleResult());

        return "game?questID=" + questID;
    }

}
