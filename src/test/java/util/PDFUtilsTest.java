package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

import es.thalesalv.bot.rpg.model.CharacterSheetVampireV20;
import es.thalesalv.bot.rpg.util.PDFUtils;

public class PDFUtilsTest {

    @Test
    public void testPopulateRandom() throws Exception {
        
    }
    
    @Test
    public void testPopulateSheetV20() throws Exception {
        CharacterSheetVampireV20 v20 = new CharacterSheetVampireV20();

        v20.setPlayerId(3231231231L);
        v20.setPlayerName("Marquinhos");
        v20.setCharacterName("Aderbal Meireles");
        v20.setChronicle("Narnia");
        v20.setNature("Vegano");
        v20.setDemeanor("Demência");
        v20.setConcept("Hipster");
        v20.setClan("SAM é braba");
        v20.setGeneration("Gol G3");
        v20.setSire("Michel Temer");
        v20.setStrength(5);
        v20.setDexterity(3);

        String destFileName = v20.generateFileName();
        PDDocument v20Sheet = v20.populateSheet();
        PDFUtils.saveDocument(v20Sheet, destFileName);
    }
}
