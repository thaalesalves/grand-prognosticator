package es.thalesalv.bot.rpg;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotChat extends ListenerAdapter {

    private Guild guild;
    private MessageChannel channel;
    private Member memberAuthor;
    private User author;
    private User thisBot;
    private Message message;
    private List<User> mentions;
    private String content;
    private String rawContent;
    private GenericFunction function;
    private static final Logger LOGGER = LoggerFactory.getLogger(BotChat.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            guild = event.getGuild();
            channel = event.getChannel();
            author = event.getAuthor();
            memberAuthor = guild.getMember(author);
            message = event.getMessage();
            mentions = message.getMentionedUsers();
            content = message.getContentDisplay();
            rawContent = message.getContentRaw();
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
                    thisBot = event.getGuild().getMemberById(GrandPrognosticator.BOT_ID).getUser();
                    if (mentions.contains(thisBot) || firstWord.equals(GrandPrognosticator.BOT_OPERATOR)) {
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

                            if (GrandPrognosticator.isAdmin(memberAuthor)) {
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
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
