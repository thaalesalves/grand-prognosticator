package es.thalesalv.bot.rpg.function.audio.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.bean.YouTube;
import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.function.GenericFunction;
import es.thalesalv.bot.rpg.model.YouTubeVideo;
import es.thalesalv.bot.rpg.util.lavaplayer.GuildMusicManager;
import es.thalesalv.bot.rpg.util.lavaplayer.PlayerManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component
@RequiredArgsConstructor
public class MusicQueue implements GenericFunction {

    private Guild guild;
    private EmbedBuilder builder;
    private GuildMusicManager musicManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicQueue.class);

    private final PlayerManager playerManager;
    private final GrandPrognosticator grandPrognosticator;
    private final YouTube youTube;

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        try {
            List<String> videos = new ArrayList<String>();
            BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
            if (queue.size() > 0) {
                Integer index = 1;
                for (AudioTrack track : queue) {
                    YouTubeVideo video = youTube.get(track.getInfo().uri);
                    videos.add("**" + index + ".** " + video.getTitle() + " de **" + video.getCreator() + "**;");
                    index++;
                }

                builder.setDescription(String.join("\n", videos));
            } else {
                builder.setDescription("Pela Palavra de Seht, a fila musical está vazia.");
            }

            builder.setTitle("Refletindo... processando... listando fila musical");
            return builder;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FactotumException(e);
        }
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        this.guild = event.getGuild();
        this.musicManager = playerManager.getGuildMusicManager(guild);
        builder = grandPrognosticator.buildBuilder(new EmbedBuilder());
        builder.setTitle("Refletindo... processando... iniciando processos sonoros...");
    }
}
