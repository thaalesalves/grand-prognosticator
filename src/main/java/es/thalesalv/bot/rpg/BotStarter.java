package es.thalesalv.bot.rpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import es.thalesalv.bot.rpg.util.GrandPrognosticator;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class BotStarter {

    public static void main(String[] args) throws Exception {
        Object[] botListeners = {
                new BotChat(),
                new BotLogger()
        };

        SpringApplication.run(BotStarter.class, args);
        JDA jda = new JDABuilder(AccountType.BOT)
                .setToken(GrandPrognosticator.BOT_TOKEN)
                .setGame(Game.watching(GrandPrognosticator.GAME_PLAYING))
                .build();
        jda.addEventListener(botListeners);
    }
}
