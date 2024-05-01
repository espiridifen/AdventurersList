package es.ucm.fdi.iw.model;

import javax.persistence.*;

import es.ucm.fdi.iw.model.compositepks.GameJoinId;
import es.ucm.fdi.iw.model.game.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "joins")
@IdClass(GameJoinId.class)
public class GameJoin {
    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Game game;
}
