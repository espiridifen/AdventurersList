package es.ucm.fdi.iw.model.compositepks;

import java.io.Serializable;
import java.util.Objects;

import es.ucm.fdi.iw.model.game.Game;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class GameSessionId implements Serializable {
    private Long id;
    private Game game;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSessionId gameSessionId = (GameSessionId) o;
        return Objects.equals(id, gameSessionId.id) &&
                Objects.equals(game, gameSessionId.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, game);
    }
}
