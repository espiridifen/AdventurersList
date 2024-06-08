package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Quest;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	// private static final Logger log = LogManager.getLogger(RootController.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

	@GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
	@GetMapping("/register")
    public String register(Model model) {
        return "register.html";
    }
    
    @PostMapping("/register")
    @Transactional
    public String registerUserAccount(@RequestParam String username,
                                        @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setRoles(User.Role.USER.name());
        
        entityManager.persist(user);

        return "redirect:/login";
    }

    @Autowired
    EntityManager entityManager;
    
    
	@GetMapping("/")
    @Transactional
    public String index(Model model) {
        List<Game> games = entityManager.createQuery("select g from Game g", Game.class).getResultList();

        List<Quest> quests = new ArrayList<>(); // Initialize the quests variable

        for (Game game : games) {
            Quest q = new Quest();
            q.setName(game.getName());
            q.setSystem(game.getGamesystem().getName());
            q.setDate(game.getDate().toString());
            q.setId(game.getId());
            quests.add(q);
        }

        model.addAttribute("availableQuests", quests);

        return "landingpage";
    }


    @GetMapping("/quest")
    @Transactional
    public String getGamePage(Model model, @RequestParam("questID") Long questID) {
        Game g = entityManager.find(Game.class, questID);
        model.addAttribute("game", g);

        return "redirect:/game?questID=" + questID;
    }

    @GetMapping("/newGame")
    public String getGamePage(Model model) {
        return "newgame";
    }
}
