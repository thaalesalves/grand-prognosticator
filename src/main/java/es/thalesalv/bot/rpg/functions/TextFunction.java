package es.thalesalv.bot.rpg.functions;

import net.dv8tion.jda.core.EmbedBuilder;

public abstract class Function {

    public abstract EmbedBuilder buildMessage(String... strings) throws Exception;
}
