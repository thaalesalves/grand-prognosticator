package es.thalesalv.bot.rpg.model.sheet;

import java.util.Set;

public class AttributeSet {

    private Set<Stat> attrs;
    private Integer startingPoints;
    private Integer limitPoints;

    public AttributeSet(Integer startingPoints, Integer limitPoints) {
        this.startingPoints = startingPoints;
        this.limitPoints = limitPoints;
    }

    public AttributeSet() {
    }
}
