package es.thalesalv.bot.rpg.util;

public class JBotConfig {

	public static final String LANG_ISO = "pt-BR";
	
	public static final String BOT_TOKEN = JBotUtils.fetchBotProperties().getProperty("discord.token");
	public static final String BOT_ID = JBotUtils.fetchBotProperties().getProperty("discord.botid");
	public static final String BOT_SECRET = JBotUtils.fetchBotProperties().getProperty("discord.secret");
	
	public static final String WATSON_API_KEY = JBotUtils.fetchBotProperties().getProperty("watson.apikey");
	public static final String WATSON_ASSISTANT_ID = JBotUtils.fetchBotProperties().getProperty("watson.assistantid");
}
