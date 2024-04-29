package es.ucm.fdi.iw.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.ucm.fdi.iw.model.Game;
import es.ucm.fdi.iw.model.GameJoin;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;

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

    @GetMapping("")
    @Transactional
    public String game(Model model, @RequestParam("questID") long questID, HttpServletRequest request) {
        TypedQuery<Game> gameSearchQuery = entityManager.createQuery("select g from Game g where g.id = :questID", Game.class);
        gameSearchQuery.setParameter("questID", questID);
        gameSearchQuery.setMaxResults(1);

        // Get user ID from their HTTP Session
        long userId = ((User)httpSession.getAttribute("u")).getId();

        TypedQuery<GameJoin> gameJoinedSearchQuery = entityManager.createQuery("select j from GameJoin j where j.user.id = :userId and j.game.id = :gameId", GameJoin.class);
        gameJoinedSearchQuery.setParameter("userId", userId);
        gameJoinedSearchQuery.setParameter("gameId", questID);
        gameJoinedSearchQuery.setMaxResults(1);
        List<GameJoin> gj = gameJoinedSearchQuery.getResultList();
        
        TypedQuery<GameJoin> gameJoins = entityManager.createQuery("select j from GameJoin j where j.game.id = :gameId", GameJoin.class);
        gameJoins.setParameter("gameId", questID);
        List<GameJoin> joinlist = gameJoins.getResultList();

        List<String> users = new ArrayList<String>();
        for (GameJoin join : joinlist) {
            users.add(join.getUser().getUsername());
        }
        
        model.addAttribute("users", users);
        model.addAttribute("game", gameSearchQuery.getSingleResult());
        model.addAttribute("userIsJoined", !gj.isEmpty()); // User has joined the game if there is some result in the query

        return "game.html";
    }

    @PostMapping("/join")
    @Transactional
	@ResponseBody
    public ResponseEntity<String> joinGameString(HttpServletResponse response, @RequestParam("gameId") long gameId) {
        TypedQuery<Game> g_query = entityManager.createQuery("select g from Game g where g.id = :gameId", Game.class);
        g_query.setParameter("gameId", gameId);
        g_query.setMaxResults(1);
        long userId = ((User)httpSession.getAttribute("u")).getId();
        TypedQuery<User> u_query = entityManager.createQuery("select u from User u where u.id = :userId", User.class);
        u_query.setParameter("userId", userId);
        u_query.setMaxResults(1);
        Game g;
        User u;

        try {
            g = g_query.getSingleResult();
            u = u_query.getSingleResult();
        }
        catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
            // return "game";
        }
        catch (Exception e) {
            log.error("Error: " +e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error");
            // return "game";
        }

        entityManager.persist(new GameJoin(u, g));

        return ResponseEntity.ok("Joined the game successfully");
        // return "game";
    }

    @PostMapping("/leave")
    @Transactional
    @ResponseBody
    public ResponseEntity<String> leaveGame(HttpServletResponse response, @RequestParam("gameId") long gameId) {
        TypedQuery<Game> gameQuery = entityManager.createQuery("select g from Game g where g.id = :gameId", Game.class);
        gameQuery.setParameter("gameId", gameId);
        gameQuery.setMaxResults(1);
        
        try {
            gameQuery.getSingleResult();
        } catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        } catch (Exception e) {
            log.error("Error: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

        long userId;
        try {
            userId = ((User) httpSession.getAttribute("u")).getId();
        } catch (Exception e) {
            log.error("Error retrieving user from session: " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        TypedQuery<GameJoin> joinQuery = entityManager.createQuery("select j from GameJoin j where j.user.id = :userId and j.game.id = :gameId", GameJoin.class);
        joinQuery.setParameter("userId", userId);
        joinQuery.setParameter("gameId", gameId);
        joinQuery.setMaxResults(1);

        GameJoin join;
        try {
            join = joinQuery.getSingleResult();
        } catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not in the game");
        } catch (Exception e) {
            log.error("Error: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

        entityManager.remove(join);

        return ResponseEntity.ok("Left the game successfully");
    }

    @PostMapping("/sendMessage")
    @Transactional
    public ResponseEntity<String> sendMessage(HttpServletResponse response, @RequestParam("gameId") long gameId, @RequestParam("message") String message) {
        TypedQuery<Game> g_query = entityManager.createQuery("select g from Game g where g.id = :gameId", Game.class);
        g_query.setParameter("gameId", gameId);
        g_query.setMaxResults(1);
        User u = (User)httpSession.getAttribute("u");
        Game g;

        try {
            g = g_query.getSingleResult();
        }
        catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
            // return "game";
        }
        catch (Exception e) {
            log.error("Error: " +e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error");
            // return "game";
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
        }
        catch (Exception e) {
            log.info(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
        log.info("Sending message to websocket "+ "/game/"+gameId+"/queue/updates"+ ": " + json);
        messagingTemplate.convertAndSend("/game/"+gameId+"/queue/updates", json);

        return ResponseEntity.ok("Message sent successfully");
        // return "game";
    }
    
    
    /**
     * Returns JSON with all received messages
     */
    @GetMapping(path = "received", produces = "application/json")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody  // para indicar que no devuelve vista, sino un objeto (jsonizado)
	public List<Message.Transfer> retrieveMessages(HttpSession session, @RequestParam("gameId") long gameId) {	
		Game game = entityManager.find(Game.class, gameId);
		return game.getReceived().stream().map(Transferable::toTransfer).collect(Collectors.toList());
	}

    /**
     * Posts a message to a game.
     * @param id of target user (source user is from ID)
     * @param o JSON-ized message, similar to {"message": "text goes here"}
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
				User.class, ((User)session.getAttribute("u")).getId());
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
		// construye json: método manual
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("from", sender.getUsername());
		rootNode.put("to", u.getUsername());
		rootNode.put("text", text);
		rootNode.put("id", m.getId());
		String json = mapper.writeValueAsString(rootNode);
		*/
		// persiste objeto a json usando Jackson
		String json = mapper.writeValueAsString(m.toTransfer());

		log.info("Sending a message to {} with contents '{}'", id, json);

		messagingTemplate.convertAndSend("/user/"+game.getId()+"/queue/updates", json);
		return "{\"result\": \"message sent.\"}";
	}	

    @PostMapping("/deleteGame")
    @Transactional
    public String deleteGame(Model model, @RequestParam("gameId") long gameId) {
        // TypedQuery<Game> gameQuery = entityManager.createQuery("select g from Game g where g.id = :gameId", Game.class);
        // gameQuery.setParameter("gameId", gameId);
        // gameQuery.setMaxResults(1);
        
        // Game game;
        // try {
        //     game = gameQuery.getSingleResult();
        // } catch (NoResultException e) {
        //     return "game";
        // } catch (Exception e) {
        //     log.error("Error: " + e);
        //     return "game";
        // }

        // long userId;
        // try {
        //     userId = ((User) httpSession.getAttribute("u")).getId();
        // } catch (Exception e) {
        //     log.error("Error retrieving user from session: " + e);
        //     return "game";
        // }

        // if (game.getOwner().getId() != userId) {
        //     return "game";
        // }

        // entityManager.remove(game);

        return "redirect:/";
    }
}
