package es.thalesalv.bot.rpg.function.audio.actions;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.bean.PlayerManager;
import es.thalesalv.bot.rpg.bean.YouTubePlaylistApi;
import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.function.GenericFunction;
import es.thalesalv.bot.rpg.model.YouTubePlaylist;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

@Component
@RequiredArgsConstructor
public class PlaylistPlay implements GenericFunction {

    private Member bot;
    private Member member;
    private User caller;
    private Guild guild;
    private GuildVoiceState state;
    private VoiceChannel channel;
    private TextChannel textChannel;
    private AudioManager manager;
    private EmbedBuilder builder;
    private String errorMessage;
    private static final String[] ALLOWED_PROTOCOLS = { "http", "https" };
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicPlay.class);

    private final YouTubePlaylistApi playlistApi;
    private final GrandPrognosticator grandPrognosticator;
    private final PlayerManager playerManager;

    private Boolean isAble() {
        if (!grandPrognosticator.isUserInVoiceChannel(state)) {
            errorMessage = "Pela Palavra de Seht, sou compelido. Conecte-se a uma sala de áudio e tente novamente.";
            return false;
        }

        VoiceChannel botChannel = bot.getVoiceState().getChannel();
        String userChannelId = channel.getId();
        if (grandPrognosticator.isConnected(manager) && !userChannelId.equals(botChannel.getId())) {
            errorMessage = "Pela Palavra de Seht, já estou conectado em uma sala. Aguarde minha disponibilidade.";
            return false;
        }

        if (!grandPrognosticator.canConnect(channel, bot) || !grandPrognosticator.canSpeak(channel, bot)) {
            errorMessage = "Pela Palavra de Seht, não tenho privilégios para acessar a sala requisitada. "
                    + "Dirija-se a um Apóstolo para requisitar meu acesso.";
            return false;
        }

        return true;
    }

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        try {
            if (!isAble()) {
                builder.setDescription(errorMessage);
                return builder;
            }

            String[] musicCommands = ArrayUtils.remove(strings, 0);
            musicCommands = ArrayUtils.remove(musicCommands, 0);
            String playlistUrl = musicCommands[1];
            UrlValidator urlValidator = new UrlValidator(ALLOWED_PROTOCOLS, UrlValidator.ALLOW_ALL_SCHEMES);

            if (!urlValidator.isValid(playlistUrl)) {
                builder.setDescription("Pela Palavra de Seht, me foi fornecida uma URL inválida.");
                return builder;
            }

            playerManager.init();
            grandPrognosticator.joinChannel(manager, channel);
            YouTubePlaylist playlist = playlistApi.get(playlistUrl);
            playlist.getVideos().forEach(video -> {
                try {
                    playerManager.loadAndPlay(textChannel, video.getUrl(), true);
                } catch (Exception e) {
                    LOGGER.error("Erro ao adicionar vídeo do YouTube à playlist: {}", e.getMessage());
                    throw new FactotumException("Erro ao adicionar vídeo do YouTube à playlist:", e);
                }
            });

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("**Playlist:** ").append(playlist.getTitle())
                    .append("\n**Criador:** ").append(playlist.getCreator())
                    .append("\n**URL:** ").append(playlist.getUrl())
                    .append("\n**Vídeos adicionados à fila:** ").append(playlist.getVideos().size());

            builder.setTitle("Refletindo... carregando... playlist adicionada à fila musical.");
            builder.setDescription(stringBuilder.toString());
            return builder;
        } catch (Exception e) {
            LOGGER.error("Erro ao tocar playlist do YouTube: {}", e.getMessage());
            throw new FactotumException("Erro ao tocar playlist do YouTube:", e);
        }
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        this.guild = event.getGuild();
        this.caller = event.getAuthor();
        this.member = guild.getMember(caller);
        this.state = member.getVoiceState();
        this.bot = guild.getSelfMember();
        this.channel = state.getChannel();
        this.textChannel = event.getTextChannel();
        this.manager = guild.getAudioManager();
        builder = grandPrognosticator.buildBuilder(new EmbedBuilder());
        builder.setTitle("Refletindo... processando... iniciando processos sonoros...");
    }
}