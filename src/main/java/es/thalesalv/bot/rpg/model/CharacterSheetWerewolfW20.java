package es.thalesalv.bot.rpg.model;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sheet_werewolfw20")
public class CharacterSheetWerewolfW20 extends Sheet {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSheetWerewolfW20.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long characterId;
    private Long playerId;
    private String playerName;
    private String characterName;

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
