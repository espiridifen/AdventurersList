package es.ucm.fdi.iw.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.game.ExperienceEnum;
import es.ucm.fdi.iw.model.game.Game;
import es.ucm.fdi.iw.model.game.GameSystemEnum;

@Controller
@RequestMapping("/busqueda")
public class SearchController {
    @Autowired
    EntityManager entityManager;

    @GetMapping("")
    public String busqueda(Model model) {
        // Cantidades de juegos segun experiencia
        model.addAttribute("cantidadBeginner", getQuantity(ExperienceEnum.BEGINNER));
        model.addAttribute("cantidadIntermediate", getQuantity(ExperienceEnum.INTERMEDIATE));
        model.addAttribute("cantidadAdvanced", getQuantity(ExperienceEnum.ADVANCED));

        // Cantidades de juegos segun version
        model.addAttribute("cantidadV1", getQuantity(GameSystemEnum.FIRST_VERSION));
        model.addAttribute("cantidadV2", getQuantity(GameSystemEnum.SECOND_VERSION));
        model.addAttribute("cantidadV3", getQuantity(GameSystemEnum.THIRD_VERSION));
        model.addAttribute("cantidadV35", getQuantity(GameSystemEnum.THIRD_VERSION_ADVANCED));
        model.addAttribute("cantidadV4", getQuantity(GameSystemEnum.FOURTH_VERSION));
        model.addAttribute("cantidadV5", getQuantity(GameSystemEnum.FIFTH_VERSION));

        return "busqueda";
    }

    @GetMapping("/gamequery")
    @Transactional
    @ResponseBody
    public List<Game.Transfer> search(
            @RequestParam("searchQuery") String query,
            @RequestParam List<ExperienceEnum> experienceFilter,
            @RequestParam List<GameSystemEnum> versionFilter) {

        return performSearch(query, experienceFilter, versionFilter);
    }

    private List<Game.Transfer> performSearch(String query, List<ExperienceEnum> experienceFilter,
            List<GameSystemEnum> versionFilter) {
        // Create a JPQL query to retrieve games containing the query string in their
        // title
        String jpql = "SELECT g FROM Game g WHERE LOWER(g.name) LIKE LOWER(:query)";

        // Add condition to filter games based on experience level
        if (experienceFilter != null && !experienceFilter.isEmpty()) {
            jpql += " AND g.experience IN :experience";
        }
        // Add condition to filter games based on gamesystem
        if (versionFilter != null && !versionFilter.isEmpty()) {
            jpql += " AND g.gamesystem IN :gamesystem";
        }

        // Create query and set parameters
        TypedQuery<Game> jpqlquery = entityManager.createQuery(jpql, Game.class)
                .setParameter("query", "%" + query.toLowerCase() + "%");

        // Set experience filter parameter if provided
        if (experienceFilter != null && !experienceFilter.isEmpty()) {
            jpqlquery.setParameter("experience", experienceFilter);
        }
        // Set gamesystem filter parameter if provided
        if (versionFilter != null && !versionFilter.isEmpty()) {
            jpqlquery.setParameter("gamesystem", versionFilter);
        }

        // Execute query
        List<Game> filteredGames = jpqlquery.getResultList();

        // Convert filtered games to transfer objects
        List<Game.Transfer> result = filteredGames.stream()
                .map(Game.Transfer::new) // Convert Game to Transfer
                .collect(Collectors.toList());

        return result;
    }

    private int getQuantity(ExperienceEnum exp) {
        return performSearch("", Arrays.asList(exp), null).size();
    }

    private int getQuantity(GameSystemEnum exp) {
        return performSearch("", null, Arrays.asList(exp)).size();
    }
}
