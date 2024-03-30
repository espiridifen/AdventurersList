package es.ucm.fdi.iw.model.compositepks;

import java.io.Serializable;
import java.util.Objects;

import es.ucm.fdi.iw.model.Game;
import es.ucm.fdi.iw.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ReviewId implements Serializable {

    private Game game;
    private User user;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return Objects.equals(game, reviewId.game) &&
                Objects.equals(user, reviewId.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, user);
    }
}
