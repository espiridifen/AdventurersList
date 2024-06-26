package es.ucm.fdi.iw.model.game;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import es.ucm.fdi.iw.model.GameJoin;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "games")
public class Game implements Transferable<Game.Transfer> {
    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;

    private @NotNull String name;

    private @NotNull String description;

    private @NotNull ExperienceEnum experience;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private @NotNull LocalDateTime date;

    private @NotNull GameSystemEnum gamesystem;

    @Column(name = "sessionquantity", columnDefinition = "INTEGER")
    private int sessionQuantity;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = User.class)
    @JoinColumn(name = "owner") // Specify the column name
    private @NotNull User owner;

    private @NotNull String type;

    private @NotNull String meeting;

    @OneToMany(mappedBy = "game")
    private List<GameJoin> joins = new ArrayList<>();

    @OneToMany(mappedBy = "gameRecipient")
    private List<Message> received = new ArrayList<>();

    // Transfer, for JSON generation for the Search menu
    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String name;
        private String description;
        private ExperienceEnum experience;
        private String date;
        private String gamesystem;
        private String owner;
        private String type;
        private String meeting;

        public Transfer(Game g) {
            this.id = g.getId();
            this.name = g.getName();
            this.description = g.getDescription();
            this.experience = g.getExperience();
            this.date = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(g.getDate());
            this.gamesystem = g.getGamesystem().getName();
            this.owner = g.getOwner().getUsername();
            this.type = g.getType();
            this.meeting = g.getMeeting();
            this.id = g.getId();
        }
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, name, description, experience,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(date),
                gamesystem.getName(), owner.getUsername(), type, meeting);
    }
}
