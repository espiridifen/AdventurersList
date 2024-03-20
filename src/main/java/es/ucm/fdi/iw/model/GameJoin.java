package es.ucm.fdi.iw.model;

import javax.persistence.*;

import es.ucm.fdi.iw.model.compositepks.GameJoinId;
import lombok.Data;

@Entity
@Data
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
