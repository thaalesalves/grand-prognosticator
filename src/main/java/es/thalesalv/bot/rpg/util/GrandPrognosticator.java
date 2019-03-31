package es.thalesalv.bot.rpg.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class GrandPrognosticator {

    public static final Integer BOT_AUDIO_VOLUME = Integer
            .parseInt(GrandPrognosticator.fetchBotProperties().getProperty("discord.bot.volume"));
    public static final String GAME_PLAYING = GrandPrognosticator.fetchBotProperties().getProperty("discord.bot.game.playing");
    public static final String BOT_OPERATOR = GrandPrognosticator.fetchBotProperties().getProperty("discord.bot.operator");
    public static final String BOT_TOKEN = GrandPrognosticator.fetchBotProperties().getProperty("discord.bot.token");
    public static final String BOT_ID = GrandPrognosticator.fetchBotProperties().getProperty("discord.bot.id");
    public static final String BOT_SECRET = GrandPrognosticator.fetchBotProperties().getProperty("discord.bot.secret");
    public static final String WATSON_API_KEY = GrandPrognosticator.fetchBotProperties().getProperty("watson.apikey");
    public static final String WATSON_ASSISTANT_ID = GrandPrognosticator.fetchBotProperties().getProperty("watson.assistantid");
    public static final String FOOTER_TEXT = GrandPrognosticator.fetchBotProperties().getProperty("discord.bot.footer.text");
    public static final String FOOTER_IMG = GrandPrognosticator.fetchBotProperties().getProperty("discord.bot.footer.image");
    public static final String DISCORD_GUILD_ID = GrandPrognosticator.fetchBotProperties().getProperty("discord.guild.id");
    public static final String DISCORD_LOG_CHANNEL_ID = GrandPrognosticator.fetchBotProperties()
            .getProperty("discord.guild.log.channel.id");
    public static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/videos?id=VIDEOID&key=APIKEY&part=PART";
    public static final String YOUTUBE_KEY = GrandPrognosticator.fetchBotProperties().getProperty("youtube.api.key");
    public static final String[] YOUTUBE_PART = {
        "snippet",
        "contentDetails",
        "statistics",
        "status"
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(GrandPrognosticator.class);

    public static void die(JDA jda) {
        jda.shutdown();
        System.exit(0);
    }

    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy 'às' HH:mm");
        return now.toString(fmt);
    }

    public static Properties fetchBotProperties() {
        Properties properties = new Properties();
        try {
            File file = ResourceUtils.getFile("classpath:bot.properties");
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return properties;
    }

    public static EmbedBuilder buildBuilder(EmbedBuilder builder) throws Exception {
        try {
            builder.setColor(Color.YELLOW);
            builder.setFooter(GrandPrognosticator.FOOTER_TEXT, GrandPrognosticator.FOOTER_IMG);
            return builder;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
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
