package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "games")
public class Game {
    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;

    private @NotNull String name;

    private @NotNull String description;

    private @NotNull String experience;

    private @NotNull LocalDateTime date;

    private @NotNull String gamesystem;

    @Column(name = "sessionquantity", columnDefinition = "INTEGER")
    private int sessionQuantity;

    @ManyToOne(cascade=CascadeType.ALL, targetEntity=User.class)
    @JoinColumn(name = "owner") // Specify the column name
    private @NotNull User owner;
    
    private @NotNull String type;

    private @NotNull String meeting;
}
