package es.ucm.fdi.iw.controller;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    
    @GetMapping("/user/{id}")
    public String perfil(@PathVariable long id, Model model) {
        User viewer = (User)httpSession.getAttribute("u");
        User u = entityManager.find(User.class, id);
        
        model.addAttribute("username", u.getUsername());
        model.addAttribute("userId", u.getId());
        model.addAttribute("isAdmin", viewer.hasRole(User.Role.ADMIN));

        // Now add the user's games
        List<Game> games = entityManager.createQuery("select g from Game g where g.owner.id = :userId", Game.class)
                                        .setParameter("userId", u.getId())
                                        .getResultList();
        List<Quest> quests = new ArrayList<>(); // Initialize the quests variable

        for (Game game : games) {
            Quest q = new Quest();
            q.setName(game.getName());
            q.setSystem(game.getGamesystem().getName());
            q.setDate(game.getDate().toString());
            q.setId(game.getId());
            quests.add(q);
        }

        // Get all games where the user has sent messages
        List<Game> gamesWithMessages = entityManager.createQuery(
            "SELECT DISTINCT m.gameRecipient FROM Message m WHERE m.sender.id = :userId", Game.class)
            .setParameter("userId", u.getId())
            .getResultList();

        // Get the user's messages in each of these games
        List<ChatLog> chatLogs = new ArrayList<>();
        for (Game game : gamesWithMessages) {
            ChatLog chatLog = new ChatLog();
            chatLog.setGame(game);
            chatLog.setMessages(entityManager.createQuery(
                "SELECT m FROM Message m WHERE m.gameRecipient.id = :gameId AND m.sender.id = :userId", Message.class)
                .setParameter("gameId", game.getId())
                .setParameter("userId", u.getId())
                .getResultList().toArray(new Message[0]));
            chatLogs.add(chatLog);
        }

        model.addAttribute("chatLogs", chatLogs);
        model.addAttribute("availableQuests", quests);

        return "perfil";
    }

    @PostMapping("/deleteMessage")
    @Transactional
    public String deleteMessage(@RequestParam("messageId") Long messageId, @RequestParam("senderId") Long senderId) {
        // Find the message entity by ID
        Message message = entityManager.find(Message.class, messageId);

        User u = (User)httpSession.getAttribute("u");

        // Only admins
        if (!u.hasRole(User.Role.ADMIN)) return "/error";

        // Check if the message exists and the user matches (por las dudas, es basicamente un assert(senderId == message.sender.id))
        if (message != null && message.getSender().getId() == senderId) {
            // Delete the message
            entityManager.remove(message);
        }

        // Redirect back to the current page
        return "redirect:/user/" + senderId.toString();
    }
}
