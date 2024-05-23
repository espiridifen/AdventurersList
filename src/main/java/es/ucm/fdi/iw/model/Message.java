package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.ucm.fdi.iw.model.game.Game;
import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * A message that users can send each other.
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Message.countUnread",
	query="SELECT COUNT(m) FROM Message m "
			+ "WHERE m.gameRecipient.id = :gameId")
})
@Data
public class Message implements Transferable<Message.Transfer> {
	
	// private static Logger log = LogManager.getLogger(Message.class);	
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private Long id;
	@ManyToOne
	@JsonIgnoreProperties({"sent", "password", "enabled", "roles", "received"}) // Exclude sent field to avoid infinite recursion
	private User sender;
	@ManyToOne
	@JsonIgnoreProperties({"owner", "name", "description", "experience", "date", "gamesystem", "sessionQuantity", "type", "meeting", "received"}) // Exclude sent field to avoid infinite recursion
	private Game gameRecipient;
	private String text;
	
	private LocalDateTime dateSent;
	
	/**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 */
    @Getter
    @AllArgsConstructor
	public static class Transfer {
		private String from;
		private long fromId;
		private String to;
		private String sent;
		private String text;
		long id;
		public Transfer(Message m) {
			this.from = m.getSender().getUsername();
			this.fromId = m.getSender().getId();
			this.to = m.getGameRecipient().getName();
			this.sent = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateSent());
			this.text = m.getText();
			this.id = m.getId();
		}
	}

	@Override
	public Transfer toTransfer() {
		String recip = gameRecipient.getName();
		return new Transfer(sender.getUsername(), sender.getId(), recip, 
			DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateSent),
			text, id
        );
    }
}
