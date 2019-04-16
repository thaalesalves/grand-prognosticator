package es.thalesalv.bot.rpg.model.sheet.enums;

public enum StatColumn {
    PHYSICAL(StatName.STRENGTH, StatName.ENDURANCE, StatName.AGILITY),
    SOCIAL(StatName.CHARISMA, StatName.MANIPULATION, StatName.LUCK),
    MENTAL(StatName.PERCEPTION, StatName.INTELLIGENCE, StatName.WILLPOWER),
    TALENTS,
    SKILLS,
    KNOWLEDGES;

    private StatName[] statNames;

    StatColumn(StatName... statNames) {
        this.statNames = statNames;
    }
}
