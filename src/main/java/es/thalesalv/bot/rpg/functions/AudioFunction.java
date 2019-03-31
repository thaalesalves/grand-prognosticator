package es.thalesalv.bot.rpg.functions;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface AudioFunction {

    public EmbedBuilder execute(String... strings) throws Exception;

    public void setUp(MessageReceivedEvent event) throws Exception;
}
