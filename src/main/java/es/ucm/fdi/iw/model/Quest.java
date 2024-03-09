package es.ucm.fdi.iw.model;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Quest {
    private String name;
    private String system;
    private String date;
}
