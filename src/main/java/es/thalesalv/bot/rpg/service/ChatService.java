package es.thalesalv.bot.rpg.service;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.bean.Watson;
import es.thalesalv.bot.rpg.exception.ChatException;
import es.thalesalv.bot.rpg.function.GenericFunction;
import es.thalesalv.bot.rpg.function.audio.ChannelJoin;
import es.thalesalv.bot.rpg.function.audio.ChannelLeave;
import es.thalesalv.bot.rpg.function.audio.MusicPlay;
import es.thalesalv.bot.rpg.function.audio.MusicQueue;
import es.thalesalv.bot.rpg.function.audio.MusicSkip;
import es.thalesalv.bot.rpg.function.audio.MusicStop;
import es.thalesalv.bot.rpg.function.text.Clear;
import es.thalesalv.bot.rpg.function.text.DiceRoll;
import es.thalesalv.bot.rpg.function.text.WatsonMessage;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Component
@RequiredArgsConstructor
public class ChatService extends ListenerAdapter {

    private Guild guild;
    private GenericFunction function;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private GrandPrognosticator grandPrognosticator;

    @Autowired
    private Watson watson;

    @Value("${bot.discord.api.id}")
    private String botId;

    @Value("${bot.discord.operator}")
    private String botOperator;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            guild = event.getGuild();
            MessageChannel channel = event.getChannel();
            User author = event.getAuthor();
            String content = event.getMessage().getContentDisplay();
            String rawContent = event.getMessage().getContentRaw();
            EmbedBuilder builder = new EmbedBuilder();
            LOGGER.info("[" + guild.getName() + "] " + author.getName() + " disse em " + channel.getName() + ": " + content);

            if (!author.isBot()) {
                //Watson.buildSession();
                String watsonReply = watson.sendMessage(rawContent.replaceAll("\n", "    LINE BREAK    ").trim());
                //Watson.closeSession();
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
                            builder = grandPrognosticator.buildBuilder(builder);
                            builder.setTitle("Refletindo... processando...");

                            if (grandPrognosticator.isAdmin(guild.getMember(author))) {
                                builder.setDescription("Pela palavra de Seht, sou compelido. Desativando.");
                                channel.sendMessage(builder.build()).complete();
                                LOGGER.warn(author.getName() + " desativou o bot. Fechando aplicação.");
                                grandPrognosticator.die(guild.getJDA());
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

    @PreDestroy
    private void die() {
        guild.getJDA().shutdown();
    }
}
