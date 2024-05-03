package es.ucm.fdi.iw.model.sessionattendance;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.compositepks.SessionAttendanceId;
import es.ucm.fdi.iw.model.gamesession.GameSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sessionatendance")
@IdClass(SessionAttendanceId.class)
public class SessionAttendance {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    private GameSession gameSession;

    private @NotNull AttendanceResponseEnum response;
}
