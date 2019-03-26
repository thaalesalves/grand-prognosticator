package es.thalesalv.bot.rpg;

import java.util.List;

import javax.security.auth.login.LoginException;

import es.thalesalv.bot.rpg.util.JBotConfig;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class StartBot extends ListenerAdapter {

	private Guild guild;
    private MessageChannel channel;
    private User author;
    private Message message;
    private List<User> mentions;
    private String content;
    private String cmd;
    private String param;
    private String op;
	
	public static void main(String[] args) throws LoginException, RateLimitedException {
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
            content = message.getContentRaw();
            cmd = content.split("\\s+")[0];
            param = content.replace(cmd + " ", "");
            System.out.printf("[%s][%s] %#s: %s%n", guild.getName(), channel.getName(), author, content);
			
			if (!author.isBot()) {
				if (event.isFromType(ChannelType.TEXT)) {
					channel.sendMessage("Testando").complete();
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
