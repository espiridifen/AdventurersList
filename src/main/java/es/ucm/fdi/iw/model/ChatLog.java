package es.ucm.fdi.iw.model;

import es.ucm.fdi.iw.model.game.Game;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatLog {
    private Game game;
    private Message[] messages;
}
