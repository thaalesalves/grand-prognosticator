package es.thalesalv.bot.rpg.bean;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.exception.MessageBuilderException;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

@Component
@NoArgsConstructor
public class GrandPrognosticator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrandPrognosticator.class);

    @Value("${bot.discord.message.footer.icon}")
    private String footerImage;

    @Value("${bot.discord.message.footer.text}")
    private String footerText;

    public void die(JDA jda) {
        jda.shutdown();
        System.exit(0);
    }

    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        return now.format(fmt);
    }

    public EmbedBuilder buildBuilder(EmbedBuilder builder) throws Exception {
        try {
            builder.setColor(Color.YELLOW);
            builder.setFooter(footerText, footerImage);
            return builder;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new MessageBuilderException("Erro ao gerar mensagem embutida.", e);
        }
    }

    public void joinChannel(AudioManager manager, VoiceChannel channel) {
        manager.openAudioConnection(channel);
    }

    public void leaveChannel(AudioManager manager) {
        manager.closeAudioConnection();
    }

    public Boolean isConnected(AudioManager manager) {
        return manager.isConnected();
    }

    public Boolean isUserInVoiceChannel(GuildVoiceState state) {
        return state.inVoiceChannel();
    }

    public Boolean canConnect(VoiceChannel channel, Member bot) {
        return bot.hasPermission(channel, Permission.VOICE_CONNECT);
    }

    public Boolean canSpeak(VoiceChannel channel, Member bot) {
        return bot.hasPermission(channel, Permission.VOICE_SPEAK);
    }

    public Boolean isAdmin(Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }
}
