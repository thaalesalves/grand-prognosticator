package es.thalesalv.bot.rpg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import es.thalesalv.bot.rpg.exception.ChatException;
import es.thalesalv.bot.rpg.functions.GenericFunction;
import es.thalesalv.bot.rpg.functions.audio.ChannelJoin;
import es.thalesalv.bot.rpg.functions.audio.ChannelLeave;
import es.thalesalv.bot.rpg.functions.audio.MusicPlay;
import es.thalesalv.bot.rpg.functions.audio.MusicQueue;
import es.thalesalv.bot.rpg.functions.audio.MusicSkip;
import es.thalesalv.bot.rpg.functions.audio.MusicStop;
import es.thalesalv.bot.rpg.functions.text.Clear;
import es.thalesalv.bot.rpg.functions.text.DiceRoll;
import es.thalesalv.bot.rpg.functions.text.WatsonMessage;
import es.thalesalv.bot.rpg.util.GrandPrognosticator;
import es.thalesalv.bot.rpg.util.Watson;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChatService extends ListenerAdapter {

    private GenericFunction function;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    @Value("${bot.discord.api.id}")
    private String botId;

    @Value("${bot.operator}")
    private String botOperator;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            Guild guild = event.getGuild();
            MessageChannel channel = event.getChannel();
            User author = event.getAuthor();
            String content = event.getMessage().getContentDisplay();
            String rawContent = event.getMessage().getContentRaw();
            EmbedBuilder builder = new EmbedBuilder();
            LOGGER.info("[" + guild.getName() + "] " + author.getName() + " disse em " + channel.getName() + ": " + content);

            if (!author.isBot()) {
                Watson.buildSession();
                String watsonReply = Watson.sendMessage(rawContent.replaceAll("\n", "    LINE BREAK    ").trim());
                Watson.closeSession();
                String[] commands = rawContent.replaceAll("\\p{Punct}", "").split(" ");
                String[] rawCommands = rawContent.split(" ");
                String firstWord = rawContent.split(" ")[0];

                if (event.isFromType(ChannelType.TEXT)) {
                    User bot = event.getGuild().getMemberById(botId).getUser();
                    if (event.getMessage().getMentionedUsers().contains(bot) || firstWord.equals(botOperator)) {
                        Boolean anotherCommand = false;
                        Boolean shouldSend = true;
                        String command = commands[1];

                        /* Funções do Bot */
                        if (command.equals("role")) {
                            String arg = commands[2];
                            function = new DiceRoll();
                            String[] dicesToRoll = arg.split("d");
                            builder = function.execute(dicesToRoll[0], dicesToRoll[1], author.getAsMention(), author.getName());
                            anotherCommand = true;
                        }

                        if (command.equals("audio")) {
                            String arg = commands[2];

                            if (arg.equals("toque")) {
                                function = new MusicPlay();
                                function.setUp(event);
                                builder = function.execute(rawCommands);
                                shouldSend = false;
                                anotherCommand = true;
                            }

                            if (arg.equals("entre")) {
                                function = new ChannelJoin();
                                function.setUp(event);
                                builder = function.execute(rawCommands);
                                anotherCommand = true;
                            }

                            if (arg.equals("proximo")) {
                                function = new MusicSkip();
                                function.setUp(event);
                                builder = function.execute(rawCommands);
                                anotherCommand = true;
                            }

                            if (arg.equals("pare")) {
                                function = new MusicStop();
                                function.setUp(event);
                                builder = function.execute(rawCommands);
                                anotherCommand = true;
                            }

                            if (arg.equals("lista")) {
                                function = new MusicQueue();
                                function.setUp(event);
                                builder = function.execute(rawCommands);
                                anotherCommand = true;
                            }

                            if (arg.equals("saia")) {
                                function = new ChannelLeave();
                                function.setUp(event);
                                builder = function.execute(rawCommands);
                                anotherCommand = true;
                            }
                        }

                        if (command.equals("apague")) {
                            function = new Clear();
                            function.setUp(event);
                            builder = function.execute(rawCommands);
                            shouldSend = false;
                            anotherCommand = false;
                        }

                        if (command.equals("morra")) {
                            builder = GrandPrognosticator.buildBuilder(builder);
                            builder.setTitle("Refletindo... processando...");

                            if (GrandPrognosticator.isAdmin(guild.getMember(author))) {
                                builder.setDescription("Pela palavra de Seht, sou compelido. Desativando.");
                                channel.sendMessage(builder.build()).complete();
                                LOGGER.warn(author.getName() + " desativou o bot. Fechando aplicação.");
                                GrandPrognosticator.die(guild.getJDA());
                            }

                            builder.setDescription("Pela palavra de Seht, sou compelido. Você não tem privilégios suficientes para me desligar.");
                            anotherCommand = true;
                        }

                        if (!anotherCommand && watsonReply != null) {
                            function = new WatsonMessage();
                            builder = function.execute(watsonReply);
                        }

                        if (shouldSend)
                            channel.sendMessage(builder.build()).complete();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ChatException("Erro tratando mensagens de chat", e);
        }
    }
}
