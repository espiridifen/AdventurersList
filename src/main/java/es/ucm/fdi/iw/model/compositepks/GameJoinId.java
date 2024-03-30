package es.ucm.fdi.iw.model.compositepks;

import java.io.Serializable;
import java.util.Objects;

import es.ucm.fdi.iw.model.Game;
import es.ucm.fdi.iw.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class GameJoinId implements Serializable {

    private User user;
    private Game game;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameJoinId gameJoinId = (GameJoinId) o;
        return Objects.equals(user, gameJoinId.user) &&
                Objects.equals(game, gameJoinId.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, game);
    }
}
