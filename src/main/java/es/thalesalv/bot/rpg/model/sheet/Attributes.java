package es.thalesalv.bot.rpg.model.sheet;

public enum Attributes {
    PHYSICAL(new AttributeSet()),
    SOCIAL(new AttributeSet()),
    MENTAL(new AttributeSet());

    private AttributeSet attr;

    private Attributes(AttributeSet attr) {
        this.attr = attr;
    }
}
