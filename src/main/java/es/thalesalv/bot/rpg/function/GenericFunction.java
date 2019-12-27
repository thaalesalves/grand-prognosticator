package es.thalesalv.bot.rpg.function;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface GenericFunction {
    public EmbedBuilder execute(String... strings) throws Exception;

    public void setUp(MessageReceivedEvent event) throws Exception;
}
