package es.ucm.fdi.iw.model.game;

public enum GameSystemEnum {
    FIRST_VERSION("Dungeons and Dragons 1st Edition"),
    SECOND_VERSION("Dungeons and Dragons 2nd Edition"),
    THIRD_VERSION("Dungeons and Dragons 3rd Edition"),
    THIRD_VERSION_ADVANCED("Dungeons and Dragons 3.5th Edition"),
    FOURTH_VERSION("Dungeons and Dragons 4th Edition"),
    FIFTH_VERSION("Dungeons and Dragons 5th Edition");

    private String name;
 
    GameSystemEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
