package es.thalesalv.bot.rpg.model.sheet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stat {

    private String name;
    private String description;
    private Integer points;
    private Integer temporaryPoints;
}
