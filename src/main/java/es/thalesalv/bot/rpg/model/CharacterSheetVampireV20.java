package es.thalesalv.bot.rpg.model;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CharacterSheetVampireV20 extends Sheet {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSheetVampireV20.class);

    /* Detalhes gerais */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long characterId;
    private Long playerId;
    private String playerName;
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

    /* Outros */
    private String humanityPath;
    private Integer humanityPathPoints;
    private String humanityPathBearing;
    private String humanityPathBearingParentheses;

    private Integer willpower;
    private Integer willpowerSquare;

    private Integer bloodPool;
    private Integer bloodPerTurn;
    private String weakness;

    @Override
    public PDDocument populateSheet() {
        try {
            PDDocument sheet;
            sheet = PDDocument.load(new File(""));
            return sheet;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
