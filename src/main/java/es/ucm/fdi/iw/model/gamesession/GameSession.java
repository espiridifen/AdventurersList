package es.ucm.fdi.iw.model.gamesession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.ucm.fdi.iw.model.game.Game;
import es.ucm.fdi.iw.model.sessionattendance.SessionAttendance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gamesession")
public class GameSession {

    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;

    @ManyToOne
    private Game game;

    private @NotNull String title;

    private @NotNull LocalDateTime date;

    private @NotNull String location;

    private String linkToGame;

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionAttendance> attendeesResponses = new ArrayList<>();

    public GameSession(Game game, String title, LocalDateTime date, String location, String linkToGame) {
        this.game = game;
        this.title = title;
        this.date = date;
        this.location = location;
        this.linkToGame = linkToGame;
    }
}
