package es.thalesalv.bot.rpg.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class V20CharacterSheet {

    /* Detalhes gerais */
    private String playerTag;
    private String playerId;
    private String characterName;
    private String chronicle;
    private String demeanor;
    private String nature;
    private String concept;
    private String clan;
    private String generation;
    private String sire;

    /* Atributos */
    private Integer strength;
    private Integer dexterity;
    private Integer stamina;

    private Integer charisma;
    private Integer manipulation;
    private Integer appearance;

    private Integer perception;
    private Integer intelligence;
    private Integer wits;

    /* Habilidades */
    private Integer alertness;
    private Integer athletics;
    private Integer awareness;
    private Integer brawl;
    private Integer empathy;
    private Integer expression;
    private Integer intimidation;
    private Integer leadership;
    private Integer streetwise;
    private Integer subterfuge;

    private Integer animalKen;
    private Integer crafts;
    private Integer drive;
    private Integer etiquette;
    private Integer firearms;
    private Integer larceny;
    private Integer melee;
    private Integer performance;
    private Integer stealth;
    private Integer survival;

    private Integer academics;
    private Integer computer;
    private Integer finance;
    private Integer investigation;
    private Integer law;
    private Integer medicine;
    private Integer occult;
    private Integer politics;
    private Integer science;
    private Integer technology;

    /* Vantagens */
    private String disciplineOne;
    private Integer disciplineOneLevel;
    private String disciplineTwo;
    private Integer disciplineTwoLevel;
    private String disciplineThree;
    private Integer disciplineThreeLevel;
    private String disciplineFour;
    private Integer disciplineFourLevel;
    private String disciplineFive;
    private Integer disciplineFiveLevel;
    private String disciplineSix;
    private Integer disciplineSixLevel;
    
    private String backgroundOne;
    private Integer backgroundOneLevel;
    private String backgroundTwo;
    private Integer backgroundTwoLevel;
    private String backgroundThree;
    private Integer backgroundThreeLevel;
    private String backgroundFour;
    private Integer backgroundFourLevel;
    private String backgroundFive;
    private Integer backgroundFiveLevel;
    private String backgroundSix;
    private Integer backgroundSixLevel;
    
    private Integer conscienceConviction;
    private Integer selfControlInstinct;
    private Integer courage;
    
    /* Other */
    private String note;
    private List<String> notes;
}