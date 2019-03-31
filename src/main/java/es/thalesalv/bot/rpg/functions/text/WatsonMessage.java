package es.thalesalv.bot.rpg.functions.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.functions.GenericFunction;
import es.thalesalv.bot.rpg.util.GrandPrognosticator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WatsonMessage implements GenericFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatsonMessage.class);

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
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

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        // TODO Auto-generated method stub
        
    }
}
