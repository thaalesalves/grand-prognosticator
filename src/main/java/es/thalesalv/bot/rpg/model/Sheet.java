package es.thalesalv.bot.rpg.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.hibernate.annotations.GenericGenerator;

import es.thalesalv.bot.rpg.util.SheetIdGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Sheet {

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
