package es.ucm.fdi.iw.model;

import javax.persistence.*;

import es.ucm.fdi.iw.model.game.Game;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reported_user", nullable = true)
    private User reportedUser; // nullable

    @ManyToOne
    @JoinColumn(name = "reported_game", nullable = true)
    private Game reportedGame; // nullable

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "reported_review_game", nullable = true),
        @JoinColumn(name = "reported_review_user", nullable = true)
    })
    private Review reportedReview; // nullable

    private String reason;
}
