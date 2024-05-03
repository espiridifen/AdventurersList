package es.ucm.fdi.iw.model.gamesession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.ucm.fdi.iw.model.compositepks.GameSessionId;
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
@IdClass(GameSessionId.class)
public class GameSession {
    
    @Id
    @ManyToOne
    private Game game;

    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;

    private @NotNull String title;
    
	private @NotNull LocalDateTime date;

	private @NotNull String location;

    @OneToMany
	@JoinColumns({
        @JoinColumn(name = "game_session_game_id", referencedColumnName = "game_id"), // Specify game_id
        @JoinColumn(name = "game_session_id", referencedColumnName = "id")
    })	
	private List<SessionAttendance> attendeesResponses = new ArrayList<>();

    public GameSession(Game game, String title, LocalDateTime date, String location) {
        this.game = game;
        this.title = title;
        this.date = date;
        this.location = location;
    }
}
