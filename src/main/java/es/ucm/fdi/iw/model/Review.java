package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import es.ucm.fdi.iw.model.compositepks.ReviewId;
import es.ucm.fdi.iw.model.game.Game;
import lombok.Data;

@Entity
@Data
@Table(name = "reviews")
@IdClass(ReviewId.class)
public class Review implements Serializable {

    @Id
    @ManyToOne
    private Game game;

    @Id
    @ManyToOne
    private User user;

    private @NotNull String text;
    private @NotNull int stars;

    public void setStars(int stars) {
        if (stars < 1 || stars > 5) throw new RuntimeException("Stars must be between 1 and 5");
        this.stars = stars;
    }
}
