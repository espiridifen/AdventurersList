package es.ucm.fdi.iw.model.gamesession;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.model.GameJoin;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.game.Game;
import es.ucm.fdi.iw.model.sessionattendance.AttendanceResponseEnum;
import es.ucm.fdi.iw.model.sessionattendance.SessionAttendance;

@Service
@Transactional
public class GameSessionService {

    @Autowired
    EntityManager entityManager;

    public void createGameSession(Game game, String title, LocalDateTime date, String location) {
        GameSession gameSession = new GameSession(game, title, date, location);
        entityManager.persist(gameSession);

        // Initialize attendeesResponses for each user in the game
        for (GameJoin gj : game.getJoins()) {
            User user = gj.getUser();
            SessionAttendance attendance = new SessionAttendance();
            attendance.setUser(user);
            attendance.setGameSession(gameSession);
            attendance.setResponse(AttendanceResponseEnum.PENDING);
            gameSession.getAttendeesResponses().add(attendance);

            // Persist the SessionAttendance object
            entityManager.persist(attendance);
        }
    }
}

