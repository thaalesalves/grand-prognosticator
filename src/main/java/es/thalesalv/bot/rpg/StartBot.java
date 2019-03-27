package es.thalesalv.bot.rpg;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.functions.DiceRoll;
import es.thalesalv.bot.rpg.util.JBotConfig;
import es.thalesalv.bot.rpg.util.Watson;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class StartBot extends ListenerAdapter {

    private Guild guild;
    private MessageChannel channel;
    private User author;
    private User thisBot;
    private Message message;
    private List<User> mentions;
    private String content;
    private String rawContent;
    private String cmd;
    private String param;
    private String op;
    private static final Logger LOGGER = LoggerFactory.getLogger(StartBot.class.getName());

    public static void main(String[] args) throws Exception {
        JDA jda = new JDABuilder(AccountType.BOT).setToken(JBotConfig.BOT_TOKEN).build();
        jda.addEventListener(new StartBot());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            guild = event.getGuild();
            channel = event.getChannel();
            author = event.getAuthor();
            message = event.getMessage();
            mentions = message.getMentionedUsers();
            content = message.getContentDisplay();
            rawContent = message.getContentRaw();
            cmd = content.split("\\s+")[0];
            param = content.replace(cmd + " ", "");
            System.out.printf("[%s][%s] %#s: %s%n", guild.getName(), channel.getName(), author, content);

            if (!author.isBot()) {
                Watson.buildSession();
                String watsonReply = Watson.sendMessage(rawContent);
                Watson.closeSession();

                if (event.isFromType(ChannelType.TEXT)) {
                    thisBot = event.getGuild().getMemberById(JBotConfig.BOT_ID).getUser();
                    if (mentions.contains(thisBot)) {
                        String[] commands = rawContent.replaceAll("\\p{Punct}", "").split(" ");
                        Boolean anotherCommand = false;

                        /* Funções do Bot */
                        if (commands[1].equals("rola") || commands[1].equals("roll")) {
                            String[] dicesToRoll = commands[2].split("d");
                            int dice = Integer.parseInt(dicesToRoll[1]);
                            int diceQty = Integer.parseInt(dicesToRoll[0]);
                            String diceRolls = String.join(", ", DiceRoll.rollDice(diceQty, dice));
                            channel.sendMessage(diceRolls).complete();
                            anotherCommand = true;
                        }

                        if (!anotherCommand && watsonReply != null) {
                            channel.sendMessage(watsonReply).complete();
                        }

                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
