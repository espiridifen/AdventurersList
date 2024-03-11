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

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "games")
public class Game { // TODO: nullables
    
    
    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;
    private String name;
    private String experience;
    private LocalDateTime date;
    private String gamesystem;
    @Column(name = "sessionquantity", columnDefinition = "INTEGER")
    private Integer sessionQuantity;
    @ManyToOne(cascade=CascadeType.ALL, targetEntity=User.class)
    @JoinColumn(name = "owner") // Specify the column name
    private @NonNull User owner;
    private String type;
    private String meeting;
}
