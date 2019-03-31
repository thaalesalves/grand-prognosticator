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
import net.dv8tion.jda.core.entities.Member;

public class JBotUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JBotConfig.class);

    public static Boolean isAdmin(Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }
    
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
            builder.setFooter(JBotConfig.FOOTER_TEXT, JBotConfig.FOOTER_IMG);
            return builder;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}