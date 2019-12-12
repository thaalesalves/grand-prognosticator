package es.thalesalv.bot.rpg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.util.GrandPrognosticator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ActivityService extends ListenerAdapter {

    private Guild guild;
    private MessageChannel channel;
    private MessageChannel generalChannel;
    private EmbedBuilder builder;
    private User author;
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);

    private void setUp(Event event) throws Exception {
        guild = event.getJDA().getGuildById(GrandPrognosticator.DISCORD_GUILD_ID);
        channel = guild.getTextChannelById(GrandPrognosticator.DISCORD_LOG_CHANNEL_ID);
        generalChannel = guild.getTextChannelById(GrandPrognosticator.DISCORD_GENERAL_CHANNEL_ID);
        builder = GrandPrognosticator.buildBuilder(new EmbedBuilder());
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        try {
            setUp(event);
            author = event.getAuthor();
            builder.setTitle("Refletindo... calculando... logando...");
            String oldMessage = event.getMessage().getContentRaw();
            MessageChannel messageChannel = event.getChannel();
            String logContent = "**Usuário:** " + author.getAsMention() + "\n**Data:** " + GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** "
                    + author.getAsMention() + " editou uma mensagem em <#" + messageChannel.getId() + ">:\n" + oldMessage;
            builder.setDescription(logContent);

            LOGGER.info(author.getName() + " editou uma mensagem em " + messageChannel.getName());
            channel.sendMessage(builder.build()).complete();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        try {
            setUp(event);
            author = event.getUser();
            Message message = event.getChannel().getMessageById(event.getMessageId()).complete();
            ReactionEmote emote = event.getReactionEmote();
            String logContent = "**Usuário:** " + author.getAsMention() + "\n**Data:** " + GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** "
                    + author.getAsMention() + " reagiu com " + emote.getName() + " à mensagem: \n" + message.getContentRaw();
            builder.setDescription(logContent);
            builder.setTitle("Refletindo... calculando... logando...");
            channel.sendMessage(builder.build()).complete();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {
            setUp(event);
            builder.setTitle("Refletindo... calculando... logando...");
            author = event.getUser();
            String logContent = "**Usuário:** " + author.getAsMention() + "\n**Data:** " + GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** "
                    + author.getAsMention() + " acaba de entrar no servidor.";
            builder.setDescription(logContent);
            channel.sendMessage(builder.build()).complete();

            String welcomeMessage = "Pela palavra de Seht, eu dou as boas-vindas a " + author.getAsMention()
                    + ". Você ainda não tem acesso a nenhuma campanha. Dirija-se a um Apóstolo Mecânico ou reaja a um dos emotes em <#"
                    + GrandPrognosticator.DISCORD_CAMPAIGNS_CHANNEL_ID + "> para ganhar acesso às salas.";
            builder.setDescription(welcomeMessage);
            builder.setTitle("Refletindo... processando... bem-vindo!");
            generalChannel.sendMessage(builder.build()).complete();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        try {
            setUp(event);
            builder.setTitle("Refletindo... calculando... logando...");
            author = event.getUser();
            String logContent = "**Usuário:** " + author.getAsMention() + "\n**Data:** " + GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** "
                    + author.getAsMention() + " acaba de sair do servidor.";
            builder.setDescription(logContent);
            channel.sendMessage(builder.build()).complete();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        try {
            setUp(event);
            User user = event.getUser();
            List<String> roles = event.getRoles().stream().map(role -> role.getAsMention()).collect(Collectors.toList());
            String rolesString = String.join(", ", roles);
            String logContent = "**Usuário:** " + user.getAsMention() + "\n**Data:** " + GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** "
                    + user.getAsMention() + " foi adicionado " + (roles.size() > 1 ? "aos grupos " : "ao grupo ") + rolesString;
            builder.setDescription(logContent);
            builder.setTitle("Refletindo... calculando... logando...");
            LOGGER.info(user.getAsMention() + " foi adicionado " + (roles.size() > 1 ? "aos grupos " : "ao grupo ") + rolesString);
            channel.sendMessage(builder.build()).complete();

            user.openPrivateChannel().queue((privateChannel) -> {
                List<String> rolesNoMention = event.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList());
                String rolesStringNoMention = String.join(", ", rolesNoMention);
                builder.setDescription("Você foi adicionado " + (roles.size() > 1 ? "aos grupos " : "ao grupo **") + rolesStringNoMention
                        + "** no servidor **" + guild.getName() + "**.");
                privateChannel.sendMessage(builder.build()).complete();
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        try {
            setUp(event);
            User user = event.getUser();
            List<String> roles = event.getRoles().stream().map(role -> role.getAsMention()).collect(Collectors.toList());
            String rolesString = String.join(", ", roles);
            String logContent = "**Usuário:** " + user.getAsMention() + "\n**Data:** " + GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** "
                    + user.getAsMention() + " foi removido " + (roles.size() > 1 ? "dos grupos " : "do grupo ") + rolesString;
            builder.setDescription(logContent);
            builder.setTitle("Refletindo... calculando... logando...");
            LOGGER.info(user.getAsMention() + " foi adicionado " + (roles.size() > 1 ? "dos grupos " : "do grupo ") + rolesString);
            channel.sendMessage(builder.build()).complete();

            user.openPrivateChannel().queue((privateChannel) -> {
                List<String> rolesNoMention = event.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList());
                String rolesStringNoMention = String.join(", ", rolesNoMention);
                builder.setDescription("Você foi removido " + (roles.size() > 1 ? "aos grupos " : "ao grupo **") + rolesStringNoMention
                        + "** no servidor **" + guild.getName() + "**.");
                privateChannel.sendMessage(builder.build()).complete();
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
        try {
            setUp(event);
            author = event.getUser();
            String oldNickname = event.getPrevNick() == null ? author.getName() : event.getPrevNick();
            String newNickname = event.getNewNick() == null ? author.getName() : event.getNewNick();

            builder.setTitle("Refletindo... calculando... logando...");
            String logContent = "**Usuário:** " + author.getAsMention() + "\n**Data:** " + GrandPrognosticator.getCurrentDateTime() + "\n**Evento:** "
                    + oldNickname + " alterou o nome para " + newNickname;
            builder.setDescription(logContent);

            LOGGER.info(author.getName() + " alterou o nickname de " + oldNickname + " para " + newNickname);
            channel.sendMessage(builder.build()).complete();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
