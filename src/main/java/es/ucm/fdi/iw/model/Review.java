package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "reviews")
public class Review implements Serializable { // TODO: primary key, non-nulls
    private @Id @ManyToOne Game game;
    private @ManyToOne User user;
    private String text;
    private Integer stars;


    public void setStars(int stars) {
        if (stars < 1 || stars > 5) throw new RuntimeException("Stars must be between 1 and 5");
        this.stars = stars;
    }
}
