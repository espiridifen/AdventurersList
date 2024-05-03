package es.ucm.fdi.iw.model.compositepks;

import java.io.Serializable;
import java.util.Objects;

import es.ucm.fdi.iw.model.GameSession;
import es.ucm.fdi.iw.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SessionAttendanceId implements Serializable {

    private User user;
    private GameSession gameSession;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionAttendanceId gameJoinId = (SessionAttendanceId) o;
        return Objects.equals(user, gameJoinId.user) &&
                Objects.equals(gameSession, gameJoinId.gameSession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, gameSession);
    }
}
