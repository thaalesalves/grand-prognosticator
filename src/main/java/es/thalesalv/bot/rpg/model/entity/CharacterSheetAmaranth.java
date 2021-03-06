package es.thalesalv.bot.rpg.model.entity;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.model.Sheet;
import es.thalesalv.bot.rpg.model.sheet.AttributeSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sheet_amaranth")
public class CharacterSheetAmaranth extends Sheet {

    private AttributeSet physical;
    private AttributeSet social;
    private AttributeSet mental;

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSheetAmaranth.class);

    @Override
    public PDDocument populateSheet() throws Exception {
        try {
            return PDDocument.load(new File(this.generateFileName()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FactotumException(e);
        }
    }

    @Override
    protected String gameName() {
        return "Amaranth";
    }

    @Override
    public String generateFileName() {
        StringBuilder builder = new StringBuilder();
        builder.append(sheetSaveDir).append("/");
        builder.append(this.getPlayerName()).append("_");
        builder.append(this.getPlayerId().intValue()).append("_");
        builder.append(this.getCharacterName()).append("_");
        builder.append(this.gameName()).append(".pdf");

        return builder.toString().replace(" ", "_").trim();
    }
}
