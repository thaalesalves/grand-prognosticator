package es.thalesalv.bot.rpg.util;

public class JBotConfig {

    public static final String GAME_PLAYING = JBotUtils.fetchBotProperties().getProperty("discord.bot.game.playing");
    public static final String BOT_OPERATOR = JBotUtils.fetchBotProperties().getProperty("discord.bot.operator");

    public static final String BOT_TOKEN = JBotUtils.fetchBotProperties().getProperty("discord.bot.token");
    public static final String BOT_ID = JBotUtils.fetchBotProperties().getProperty("discord.bot.id");
    public static final String BOT_SECRET = JBotUtils.fetchBotProperties().getProperty("discord.bot.secret");

    public static final String WATSON_API_KEY = JBotUtils.fetchBotProperties().getProperty("watson.apikey");
    public static final String WATSON_ASSISTANT_ID = JBotUtils.fetchBotProperties().getProperty("watson.assistantid");

    public static final String FOOTER_TEXT = JBotUtils.fetchBotProperties().getProperty("discord.bot.footer.text");
    public static final String FOOTER_IMG = JBotUtils.fetchBotProperties().getProperty("discord.bot.footer.image");

    public static final String DISCORD_GUILD_ID = JBotUtils.fetchBotProperties().getProperty("discord.guild.id");
    public static final String DISCORD_LOG_CHANNEL_ID = JBotUtils.fetchBotProperties().getProperty("discord.guild.log.channel.id");
}
