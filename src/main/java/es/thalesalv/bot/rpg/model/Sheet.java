package es.thalesalv.bot.rpg.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import es.thalesalv.bot.rpg.util.SheetIdGenerator;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Sheet {

    @Value("${bot.rpg.sheet.dir.save}")
    protected String sheetSaveDir;

    @Value("${bot.rpg.sheet.dir.load}")
    protected String sheetLoadDir;

    @Id
    @GeneratedValue(generator = SheetIdGenerator.generatorName)
    @GenericGenerator(name = SheetIdGenerator.generatorName, strategy = "es.thalesalv.bot.rpg.util.SheetIdGenerator")
    private Long characterId;
    private Long playerId;
    private String playerName;
    private String characterName;

    public abstract PDDocument populateSheet();
    public abstract String generateFileName();
    protected abstract String gameName();
}
