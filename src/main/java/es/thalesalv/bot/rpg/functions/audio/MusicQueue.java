package es.thalesalv.bot.rpg.functions.audio;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.functions.AudioFunction;
import es.thalesalv.bot.rpg.model.YouTubeVideo;
import es.thalesalv.bot.rpg.util.GrandPrognosticator;
import es.thalesalv.bot.rpg.util.YouTube;
import es.thalesalv.bot.rpg.util.lavaplayer.GuildMusicManager;
import es.thalesalv.bot.rpg.util.lavaplayer.PlayerManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicQueue implements AudioFunction {

    private Guild guild;
    private EmbedBuilder builder;
    private PlayerManager playerManager;
    private GuildMusicManager musicManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicQueue.class);

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        try {
            List<String> videos = new ArrayList<String>();
            new Object() {
                int runCount = 0;
                {
                    musicManager.scheduler.getQueue().stream().peek(x -> runCount++).forEach(track -> {
                        YouTubeVideo video = YouTube.get(track.getInfo().uri);
                        videos.add("**" + runCount + ".** " + video.getTitle() + " de **" + video.getCreator() + "**;");
                    });
                }
            };

            builder.setTitle("Refletindo... processando... listando fila musical");
            builder.setDescription(String.join("\n", videos));
            return builder;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        this.guild = event.getGuild();
        this.playerManager = PlayerManager.getInstance();
        this.musicManager = playerManager.getGuildMusicManager(guild);
        builder = GrandPrognosticator.buildBuilder(new EmbedBuilder());
        builder.setTitle("Refletindo... processando... iniciando processos sonoros...");
    }
}
