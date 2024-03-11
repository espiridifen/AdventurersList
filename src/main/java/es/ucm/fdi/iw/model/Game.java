package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@Table(name = "games")
public class Game { // TODO: nullables
    
    
    private @GeneratedValue @Id Long id;
    private String name;
    private String experience;
    private LocalDateTime date;
    private String gamesystem;
    private Integer sessionQuantity;
    private @NonNull @ManyToOne User owner;
    private String type;
    private String meeting;
}
