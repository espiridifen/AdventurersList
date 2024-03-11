package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "reports")
public class Report implements Serializable {
    private @Id @ManyToOne User user;
    private String reason;
    private @ManyToOne User reportedUser; // nullable
    private @ManyToOne Game reportedGame; // nullable
    private @ManyToOne Review reportedReview; // nullable
}
