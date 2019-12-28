package es.thalesalv.bot.rpg.function.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.function.GenericFunction;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component
@RequiredArgsConstructor
public class Shutdown implements GenericFunction {

    private User author;
    private Guild guild;
    private MessageChannel channel;
    private static final Logger LOGGER = LoggerFactory.getLogger(Shutdown.class);

    private static final String UNAUTHORIZED = "{} tentou desligar o bot de {} na guilda {}, mas não tem permissão para tal.";
    private static final String UNAUTHORIZED_MESSAGE = "Pela Palavra de Seht, sou compelido. Você não tem privilégios suficientes para me desligar.";
    private static final String SHUTTING_DOWN = "Sendo desligado por {} em {} na guilda {}";

    private final GrandPrognosticator grandPrognosticator;

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {

        EmbedBuilder builder = new EmbedBuilder();
        builder = grandPrognosticator.buildBuilder(builder);
        builder.setTitle("Refletindo... processando...");
        if (grandPrognosticator.isAdmin(guild.getMember(author))) {
            builder.setDescription("Pela Palavra de Seht, sou compelido. Desativando.");
            channel.sendMessage(builder.build()).complete();
            LOGGER.warn(SHUTTING_DOWN, author.getName(), channel.getName(), guild.getName());
            grandPrognosticator.die(guild.getJDA());
        } else {
            LOGGER.warn(UNAUTHORIZED, author.getName(), channel.getName(), guild.getName());
            builder.setDescription(UNAUTHORIZED_MESSAGE);
        }

        return grandPrognosticator.buildBuilder(builder);
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        channel = event.getChannel();
        author = event.getAuthor();
        guild = event.getGuild();
    }
}