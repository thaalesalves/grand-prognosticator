package es.thalesalv.bot.rpg.function.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.function.GenericFunction;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component
public class WatsonMessage implements GenericFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatsonMessage.class);

    @Autowired
    private GrandPrognosticator grandPrognosticator;

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        try {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Refletindo... calculando... prognóstico insatisfatório.");
            builder.setDescription(strings[0]);
            return grandPrognosticator.buildBuilder(builder);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FactotumException(e);
        }
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {}
}
