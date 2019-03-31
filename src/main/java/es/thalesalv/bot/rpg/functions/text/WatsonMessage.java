package es.thalesalv.bot.rpg.functions.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.functions.TextFunction;
import es.thalesalv.bot.rpg.util.GrandPrognosticator;
import net.dv8tion.jda.core.EmbedBuilder;

public class WatsonMessage extends TextFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatsonMessage.class);

    @Override
    public EmbedBuilder buildMessage(String... strings) throws Exception {
        try {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Refletindo... calculando... prognóstico insatisfatório.");
            builder.setDescription(strings[0]);
            return GrandPrognosticator.buildBuilder(builder);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
