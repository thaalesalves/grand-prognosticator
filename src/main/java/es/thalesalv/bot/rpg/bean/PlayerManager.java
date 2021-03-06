package es.thalesalv.bot.rpg.bean;

import java.text.ParseException;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.model.YouTubeVideo;
import es.thalesalv.bot.rpg.util.lavaplayer.GuildMusicManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

@Component
@RequiredArgsConstructor
public class PlayerManager {

    private AudioPlayerManager audioPlayerManager;

    private static final String ERROR_PLAYING = "Pela Palavra de Seht, não foi possível tocar a canção pedida. Apresente-se a um Apóstolo Mecânico com a seguinte pilha: ";
    private static final String NOT_IMPLEMENTED = "Função ainda não implementada. Não é possível reproduzir playlists.";
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerManager.class);

    private final YouTubeVideoApi youTube;
    private final GrandPrognosticator grandPrognosticator;
    private final Map<Long, GuildMusicManager> musicManagers;

    @Value("${bot.discord.volume}")
    private String botVolume;

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(audioPlayerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, boolean isPlaylist) throws Exception {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        EmbedBuilder builder = grandPrognosticator.buildBuilder(new EmbedBuilder());
        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                try {
                    musicManager.getPlayer().setVolume(Integer.parseInt(botVolume));
                    StringBuilder stringBuilder = new StringBuilder();
                    if (!isPlaylist) {
                        YouTubeVideo video = youTube.get(trackUrl);
                        stringBuilder.append("**Título:** ").append(video.getTitle()).append("\n**Canal:** ")
                                .append(video.getCreator()).append("\n**URL:** ").append(video.getUrl())
                                .append("\n**Visualizações:** ").append(video.getViewCount()).append("\n**Curtidas:** ")
                                .append(video.getLikeCount()).append("\n**Publicação:** ")
                                .append(video.parsePublishedAt());

                        builder.setTitle("Refletindo... carregando... Pela Palavra de Seht, adiciono a canção à fila.");
                        builder.setDescription(stringBuilder.toString());
                        channel.sendMessage(builder.build()).complete();
                    }

                    play(musicManager, track);
                } catch (ParseException e) {
                    LOGGER.error(e.getMessage());
                    throw new FactotumException(e);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                loadFailed(new FriendlyException(NOT_IMPLEMENTED, null, null));
                channel.sendMessage(builder.build()).complete();
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
                builder.setDescription(ERROR_PLAYING + exception.getMessage());
                channel.sendMessage(builder.build()).complete();
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.getScheduler().queue(track);
    }

    public void init() {
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }
}