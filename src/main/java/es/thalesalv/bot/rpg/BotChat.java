package es.thalesalv.bot.rpg;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.functions.DiceRoll;
import es.thalesalv.bot.rpg.functions.Function;
import es.thalesalv.bot.rpg.functions.Music;
import es.thalesalv.bot.rpg.functions.WatsonMessage;
import es.thalesalv.bot.rpg.util.JBotConfig;
import es.thalesalv.bot.rpg.util.JBotUtils;
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
    private Function function;
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
                String firstWord = rawContent.split(" ")[0];

                if (event.isFromType(ChannelType.TEXT)) {
                    thisBot = event.getGuild().getMemberById(JBotConfig.BOT_ID).getUser();
                    if (mentions.contains(thisBot) || firstWord.equals(JBotConfig.BOT_OPERATOR)) {
                        Boolean anotherCommand = false;

                        /* Funções do Bot */
                        if (commands[1].equals("role")) {
                            function = new DiceRoll();
                            String[] dicesToRoll = commands[2].split("d");
                            builder = function.buildMessage(dicesToRoll[0], dicesToRoll[1], author.getAsMention(),
                                    author.getName());
                            anotherCommand = true;
                        }

                        if (commands[1].equals("audio")) {
                            function = new Music(event);
                            String[] musicCommands = ArrayUtils.remove(commands, 0);
                            musicCommands = ArrayUtils.remove(musicCommands, 0);
                            builder = function.buildMessage(musicCommands);
                            anotherCommand = true;
                        }

                        if (commands[1].equals("morra")) {
                            builder = JBotUtils.buildBuilder(builder);
                            builder.setTitle("Refletindo... processando...");

                            if (JBotUtils.isAdmin(memberAuthor)) {
                                builder.setDescription("Pela palavra de Seht, sou compelido. Desativando.");
                                channel.sendMessage(builder.build()).complete();
                                LOGGER.warn(author.getName() + " desativou o bot. Fechando aplicação.");
                                JBotUtils.die(guild.getJDA());
                            }

                            builder.setDescription(
                                    "Pela palavra de Seht, sou compelido. Você não tem privilégios suficientes para me desligar.");
                            anotherCommand = true;
                        }

                        if (!anotherCommand && watsonReply != null) {
                            function = new WatsonMessage();
                            builder = function.buildMessage(watsonReply);
                        }

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
