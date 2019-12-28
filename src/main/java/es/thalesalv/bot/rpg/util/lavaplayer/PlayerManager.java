package es.thalesalv.bot.rpg.util.lavaplayer;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.bean.YouTube;
import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.model.YouTubeVideo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

@RequiredArgsConstructor
public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerManager.class);

    @NonNull
    private GrandPrognosticator grandPrognosticator;

    @NonNull
    private YouTube youTube;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) throws Exception {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        EmbedBuilder builder = grandPrognosticator.buildBuilder(new EmbedBuilder());
        YouTubeVideo video = youTube.get(trackUrl);
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                try {
                    builder.setTitle("Refletindo... carregando... Pela Palavra de Seht, adiciono a canção à fila.");
                    builder.setDescription("**Título:** " + video.getTitle() + "\n**Canal:** " + video.getCreator()
                            + "\n**URL:** " + video.getUrl() + "\n**Visualizações:** " + video.getViewCount()
                            + "\n**Curtidas:** " + video.getLikeCount() + "\n**Publicação:** "
                            + video.parsePublishedAt());
                    channel.sendMessage(builder.build()).complete();

                    play(musicManager, track);
                } catch (ParseException e) {
                    LOGGER.error(e.getMessage());
                    throw new FactotumException(e);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                loadFailed(new FriendlyException("Função ainda não implementada. Não é possível reproduzir playlists.",
                        null, new NotImplementedException(
                                "Função ainda não implementada. Não é possível reproduzir playlists.")));
            }

            @Override
            public void noMatches() {
                builder.setTitle("Refletindo... carregando... Erro na requisição.");
                builder.setDescription("Pela Palavra de Seht, não encontrei resultados no URL fornecido: " + trackUrl);
                channel.sendMessage(builder.build()).complete();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                builder.setTitle("Refletindo... carregando... Erro na requisição.");
                builder.setDescription(
                        "Pela Palavra de Seht, não foi possível tocar a canção pedida. Apresente-se a um Apóstolo Mecânico com a seguinte pilha: "
                                + exception.getMessage());
                channel.sendMessage(builder.build()).complete();
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}