package es.thalesalv.bot.rpg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class JBotConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JBotConfig.class.getName());

	public static final String BOT_TOKEN = JBotConfig.fetchBotProperties().getProperty("discord.token");
	public static final String BOT_ID = JBotConfig.fetchBotProperties().getProperty("discord.botid");
	public static final String BOT_SECRET = JBotConfig.fetchBotProperties().getProperty("discord.secret");
	
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
}
