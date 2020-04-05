package es.thalesalv.bot.rpg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import es.thalesalv.bot.rpg.service.ActivityService;
import es.thalesalv.bot.rpg.service.ChatService;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

@SpringBootApplication
public class GrandPrognosticatorApplication {

    private static String API_TOKEN;
    private static String GAME_PLAYING;
    private static ChatService CHAT_SERVICE;
    private static ActivityService ACTIVITY_SERVICE;

    @Autowired
    private void setActivityService(ActivityService activityService) {
        ACTIVITY_SERVICE = activityService;
    }

    @Autowired
    private void setChatService(ChatService chatService) {
        CHAT_SERVICE = chatService;
    }

    @Value("${bot.discord.api.token}")
    private void setApiToken(String apiToken) {
        API_TOKEN = apiToken;
    }

    @Value("${bot.discord.playing}")
    private void setGamePlaying(String gamePlaying) {
        GAME_PLAYING = gamePlaying;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GrandPrognosticatorApplication.class, args);
        JDA jda = new JDABuilder(AccountType.BOT)
            .setToken(API_TOKEN)
            .setGame(Game.watching(GAME_PLAYING))
            .build();

        jda.addEventListener(CHAT_SERVICE, ACTIVITY_SERVICE);
    }
}
