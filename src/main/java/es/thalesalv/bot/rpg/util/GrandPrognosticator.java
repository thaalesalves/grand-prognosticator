package es.thalesalv.bot.rpg.util;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import es.thalesalv.bot.rpg.exception.MessageBuilderException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class GrandPrognosticator {

    private static String FOOTER_IMG;
    private static String FOOTER_TEXT;
    private static final Logger LOGGER = LoggerFactory.getLogger(GrandPrognosticator.class);

    @Value("${bot.discord.message.footer.icon}")
    private void setFooterImage(String footerImage) {
        FOOTER_IMG = footerImage;
    }

    @Value("${bot.discord.message.footer.text}")
    private void setFooterText(String footerText) {
        FOOTER_TEXT = footerText;
    }

    public static void die(JDA jda) {
        jda.shutdown();
        System.exit(0);
    }

    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        return now.format(fmt);
    }

    public static EmbedBuilder buildBuilder(EmbedBuilder builder) throws Exception {
        try {
            builder.setColor(Color.YELLOW);
            builder.setFooter(GrandPrognosticator.FOOTER_TEXT, GrandPrognosticator.FOOTER_IMG);
            return builder;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new MessageBuilderException("Erro ao gerar mensagem embutida.", e);
        }
    }

    public static void joinChannel(AudioManager manager, VoiceChannel channel) {
        manager.openAudioConnection(channel);
    }

    public static void leaveChannel(AudioManager manager) {
        manager.closeAudioConnection();
    }

    public static Boolean isConnected(AudioManager manager) {
        return manager.isConnected();
    }

    public static Boolean isUserInVoiceChannel(GuildVoiceState state) {
        return state.inVoiceChannel();
    }

    public static Boolean canConnect(VoiceChannel channel, Member bot) {
        return bot.hasPermission(channel, Permission.VOICE_CONNECT);
    }

    public static Boolean canSpeak(VoiceChannel channel, Member bot) {
        return bot.hasPermission(channel, Permission.VOICE_SPEAK);
    }

    public static Boolean isAdmin(Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }
}
