package es.thalesalv.bot.rpg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.util.GrandPrognosticator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.self.SelfUpdateNameEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotLogger extends ListenerAdapter {

	private Guild guild;
	private MessageChannel channel;
	private EmbedBuilder builder;
	private User author;
	private static final Logger LOGGER = LoggerFactory.getLogger(BotLogger.class);

	private void setGuild(Event event) throws Exception {
		guild = event.getJDA().getGuildById(GrandPrognosticator.DISCORD_GUILD_ID);
		channel = guild.getTextChannelById(GrandPrognosticator.DISCORD_LOG_CHANNEL_ID);
		builder = GrandPrognosticator.buildBuilder(new EmbedBuilder());
	}

	@Override
	public void onSelfUpdateName(SelfUpdateNameEvent event) {
		try {
			setGuild(event);
			author = event.getSelfUser();
			String oldNickname = event.getOldName();
			String newNickname = event.getNewName();

			builder.setTitle("Refletindo... calculando... logando...");
			String logContent = "**Usuário:** " + author.getAsMention() + "\n**Data:** "
					+ GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** " + oldNickname + " alterou o nome para "
					+ newNickname;
			builder.setDescription(logContent);

			LOGGER.info(author.getName() + " alterou o nickname de " + oldNickname + " para " + newNickname);
			channel.sendMessage(builder.build()).complete();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUserUpdateName(UserUpdateNameEvent event) {
		try {
			setGuild(event);
			author = event.getUser();
			String oldNickname = event.getOldName();
			String newNickname = event.getNewName();

			builder.setTitle("Refletindo... calculando... logando...");
			String logContent = "**Usuário:** " + author.getAsMention() + "\n**Data:** "
					+ GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** " + oldNickname + " alterou o nome para "
					+ newNickname;
			builder.setDescription(logContent);

			LOGGER.info(author.getName() + " alterou o nickname de " + oldNickname + " para " + newNickname);
			channel.sendMessage(builder.build()).complete();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		try {
			setGuild(event);
			author = event.getAuthor();
			builder.setTitle("Refletindo... calculando... logando...");
			String oldMessage = event.getMessage().getContentRaw();
			MessageChannel messageChannel = event.getChannel();
			String logContent = "**Usuário:** " + author.getAsMention() + "\n**Data:** "
					+ GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** " + author.getAsMention()
					+ " editou uma mensagem em <#" + messageChannel.getId() + ">:\n" + oldMessage;
			builder.setDescription(logContent);

			LOGGER.info(author.getName() + " editou uma mensagem em " + messageChannel.getName());
			channel.sendMessage(builder.build()).complete();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
	    try {
	        
	    } catch (Exception e) {
	        LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
	    }
	}
}
