package es.ucm.fdi.iw.model;

import java.util.Set;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "joins")
public class GameJoin { // TODO: composite primary keys, non-null
    private @Id Long id;
    
    @ManyToMany(cascade=CascadeType.ALL, targetEntity=User.class)
    private Set<User> user;

    @ManyToMany(cascade=CascadeType.ALL, targetEntity=Game.class)
    private Set<Game> game;

}
