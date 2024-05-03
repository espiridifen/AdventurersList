package es.ucm.fdi.iw.controller;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ucm.fdi.iw.model.ChatLog;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Quest;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.game.Game;

@Controller
public class ProfileController {
	private static final Logger log = LogManager.getLogger(ProfileController.class);
    
    @Autowired
    EntityManager entityManager;

    @Autowired
    HttpSession httpSession;
    
    @GetMapping("/perfil")
    public String perfil(Model model) {
        User u = (User)httpSession.getAttribute("u");
        
        model.addAttribute("username", u.getUsername());
        model.addAttribute("userId", u.getId());
        model.addAttribute("isAdmin", u.hasRole(User.Role.ADMIN));

        // Now add the user's games
        List<Game> games = entityManager.createQuery("select g from Game g where g.owner.id = :userId", Game.class)
                                        .setParameter("userId", u.getId())
                                        .getResultList();
        List<Quest> quests = new ArrayList<>(); // Initialize the quests variable

        for (Game game : games) {
            Quest q = new Quest();
            q.setName(game.getName());
            q.setSystem(game.getGamesystem().toString());
            q.setDate(game.getDate().toString());
            q.setId(game.getId());
            quests.add(q);
        }

        // Get the users messages in each of the games they are in
        List<ChatLog> chatLogs = new ArrayList<>();
        for (Game game : games) {
            ChatLog chatLog = new ChatLog();
            chatLog.setGame(game);
            chatLog.setMessages(entityManager.createQuery("select m from Message m where m.gameRecipient.id = :gameId and m.sender.id = :userId", Message.class)
                                            .setParameter("gameId", game.getId())
                                            .setParameter("userId", u.getId())
                                            .getResultList().toArray(new Message[0]));
            chatLogs.add(chatLog);
        }

        model.addAttribute("chatLogs", chatLogs);

        model.addAttribute("availableQuests", quests);


        return "perfil";
    }
}
