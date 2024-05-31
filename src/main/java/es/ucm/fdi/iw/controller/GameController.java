package es.ucm.fdi.iw.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.GameJoin;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.game.Game;
import es.ucm.fdi.iw.model.gamesession.GameSession;

@Controller
@RequestMapping("/game")
public class GameController {

    private static final Logger log = LogManager.getLogger(GameController.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    HttpSession httpSession;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private LocalData localData;

    @GetMapping("")
    @Transactional
    public String game(Model model, @RequestParam("questID") long gameId, HttpServletRequest request) {
        Game g = entityManager.find(Game.class, gameId);

        // Get user ID from their HTTP Session
        long userId = ((User) httpSession.getAttribute("u")).getId();

        TypedQuery<GameJoin> gameJoinedSearchQuery = entityManager.createQuery(
                "select j from GameJoin j where j.user.id = :userId and j.game.id = :gameId", GameJoin.class);
        gameJoinedSearchQuery.setParameter("userId", userId);
        gameJoinedSearchQuery.setParameter("gameId", gameId);
        gameJoinedSearchQuery.setMaxResults(1);
        List<GameJoin> gj = gameJoinedSearchQuery.getResultList();

        TypedQuery<GameJoin> gameJoins = entityManager.createQuery("select j from GameJoin j where j.game.id = :gameId",
                GameJoin.class);
        gameJoins.setParameter("gameId", gameId);
        List<GameJoin> joinlist = gameJoins.getResultList();

        List<String> users = new ArrayList<String>();
        for (GameJoin join : joinlist) {
            users.add(join.getUser().getUsername());
        }

        model.addAttribute("users", users);
        model.addAttribute("game", g);
        model.addAttribute("userIsJoined", !gj.isEmpty()); // User has joined the game if there is some result in the
                                                           // query
        model.addAttribute("userId", userId);
        // get the earliest gameSession
        TypedQuery<GameSession> sessionQuery = entityManager.createQuery(
                "select gs from GameSession gs where gs.game.id = :gameId order by gs.date asc",
                GameSession.class);
        sessionQuery.setParameter("gameId", gameId);
        sessionQuery.setMaxResults(1);
        // also get all sessions
        TypedQuery<GameSession> allsessionQuery = entityManager.createQuery(
                "select gs from GameSession gs where gs.game.id = :gameId order by gs.date asc",
                GameSession.class);
        allsessionQuery.setParameter("gameId", gameId);

        try {
            GameSession gs = sessionQuery.getSingleResult();
            model.addAttribute("attendanceData", gs);

            List<GameSession> allSessions = allsessionQuery.getResultList();
            model.addAttribute("allSessions", allSessions);

            model.addAttribute("arethereAnySessions", true);
        } catch (NoResultException e) {
            model.addAttribute("arethereAnySessions", false);
        }

        model.addAttribute("isOwner", userId == g.getOwner().getId());

        return "game.html";
    }

    @PostMapping("/join")
    @Transactional
    @ResponseBody
    public ResponseEntity<String> joinGameString(HttpServletResponse response, @RequestParam("gameId") long gameId) {
        Game g = entityManager.find(Game.class, gameId);
        long userId = ((User) httpSession.getAttribute("u")).getId();
        User u = entityManager.find(User.class, userId);

        entityManager.persist(new GameJoin(u, g));

        return ResponseEntity.ok("Joined the game successfully");
    }

    @PostMapping("/leave")
    @Transactional
    @ResponseBody
    public ResponseEntity<String> leaveGame(HttpServletResponse response, @RequestParam("gameId") long gameId) {
        Game g = entityManager.find(Game.class, gameId);
        long userId = ((User) httpSession.getAttribute("u")).getId();

        if (g == null)
            return ResponseEntity.status(400).body("Game does not exist");
        TypedQuery<GameJoin> joinQuery = entityManager.createQuery(
                "select j from GameJoin j where j.user.id = :userId and j.game.id = :gameId", GameJoin.class);
        joinQuery.setParameter("userId", userId);
        joinQuery.setParameter("gameId", gameId);
        joinQuery.setMaxResults(1);

        List<GameJoin> joins = joinQuery.getResultList();
        if (joins.size() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not in the game");

        entityManager.remove(joins.get(0));

        return ResponseEntity.ok("Left the game successfully");
    }

    @PostMapping("/sendMessage")
    @Transactional
    public ResponseEntity<String> sendMessage(HttpServletResponse response, @RequestParam("gameId") long gameId,
            @RequestParam("message") String message) {
        Game g = entityManager.find(Game.class, gameId);
        User u = (User) httpSession.getAttribute("u");

        if (g == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        Message m = new Message();
        // m.setId(null);
        m.setSender(u);
        m.setGameRecipient(g);
        m.setText(message);
        m.setDateSent(LocalDateTime.now());

        entityManager.persist(m); // Persist now to get ID to send to the websocket
        String json;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            json = mapper.writeValueAsString(m);
        } catch (Exception e) {
            log.info(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
        log.info("Sending message to websocket " + "/game/" + gameId + "/queue/updates" + ": " + json);
        messagingTemplate.convertAndSend("/game/" + gameId + "/queue/updates", json);

        return ResponseEntity.ok("Message sent successfully");
    }

    /**
     * Returns JSON with all received messages
     */
    @GetMapping(path = "received", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // para indicar que no devuelve vista, sino un objeto (jsonizado)
    public List<Message.Transfer> retrieveMessages(HttpSession session, @RequestParam("gameId") long gameId) {
        Game game = entityManager.find(Game.class, gameId);
        return game.getReceived().stream().map(Transferable::toTransfer).collect(Collectors.toList());
    }

    /**
     * Posts a message to a game.
     * 
     * @param id of target user (source user is from ID)
     * @param o  JSON-ized message, similar to {"message": "text goes here"}
     * @throws JsonProcessingException
     */
    @PostMapping("/{id}/msg")
    @ResponseBody
    @Transactional
    public String postMsg(@PathVariable long id,
            @RequestBody JsonNode o, Model model, HttpSession session)
            throws JsonProcessingException {

        String text = o.get("message").asText();
        Game game = entityManager.find(Game.class, id);
        User sender = entityManager.find(
                User.class, ((User) session.getAttribute("u")).getId());
        model.addAttribute("user", game);

        // construye mensaje, lo guarda en BD
        Message m = new Message();
        m.setGameRecipient(game);
        m.setSender(sender);
        m.setDateSent(LocalDateTime.now());
        m.setText(text);
        entityManager.persist(m);
        entityManager.flush(); // to get Id before commit

        ObjectMapper mapper = new ObjectMapper();
        /*
         * // construye json: método manual
         * ObjectNode rootNode = mapper.createObjectNode();
         * rootNode.put("from", sender.getUsername());
         * rootNode.put("to", u.getUsername());
         * rootNode.put("text", text);
         * rootNode.put("id", m.getId());
         * String json = mapper.writeValueAsString(rootNode);
         */
        // persiste objeto a json usando Jackson
        String json = mapper.writeValueAsString(m.toTransfer());

        log.info("Sending a message to {} with contents '{}'", id, json);

        messagingTemplate.convertAndSend("/user/" + game.getId() + "/queue/updates", json);
        return "{\"result\": \"message sent.\"}";
    }

    @PostMapping("/deleteGame")
    @Transactional
    public String deleteGame(Model model, @RequestParam("gameId") long gameId) {
        // Get user ID from their HTTP Session
        long userId = ((User) httpSession.getAttribute("u")).getId();

        Game game = entityManager.find(Game.class, gameId);

        if (game != null && game.getOwner().getId() == userId) {
            // Delete all messages related to this game
            entityManager.createQuery("DELETE FROM Message m WHERE m.gameRecipient = :game")
                    .setParameter("game", game)
                    .executeUpdate();

            // Find all GameSessions related to this game
            List<GameSession> gameSessions = entityManager
                    .createQuery("SELECT q FROM GameSession q WHERE q.game = :game", GameSession.class)
                    .setParameter("game", game)
                    .getResultList();

            // Delete all SessionAttendance entities related to each GameSession
            for (GameSession gameSession : gameSessions) {
                entityManager.createQuery("DELETE FROM SessionAttendance sa WHERE sa.gameSession = :gameSession")
                        .setParameter("gameSession", gameSession)
                        .executeUpdate();
            }

            // Delete all GameSessions related to this game
            entityManager.createQuery("DELETE FROM Report q WHERE q.reportedGame = :game")
                    .setParameter("game", game)
                    .executeUpdate();

            // Delete all GameSessions related to this game
            entityManager.createQuery("DELETE FROM GameSession q WHERE q.game = :game")
                    .setParameter("game", game)
                    .executeUpdate();

            // Delete all GameJoin entities related to this game
            entityManager.createQuery("DELETE FROM GameJoin q WHERE q.game = :game")
                    .setParameter("game", game)
                    .executeUpdate();

            // Delete the Game
            entityManager.createQuery("DELETE FROM Game q WHERE q.id = :gameId")
                    .setParameter("gameId", gameId)
                    .executeUpdate();
        }
        log.info("Deleted game: " + gameId);

        return "redirect:/";
    }

    /**
     * Returns the default profile pic
     * 
     * @return
     */
    private static InputStream defaultPic() {
        return new BufferedInputStream(Objects.requireNonNull(
                UserController.class.getClassLoader().getResourceAsStream(
                        "static/img/default-pic.jpg")));
    }

    /**
     * Downloads a profile pic for a game id
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping("{id}/pic")
    public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
        File f = localData.getFile("game", "" + id + ".png");
        InputStream in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : GameController.defaultPic());
        return os -> FileCopyUtils.copy(in, os);
    }
}
