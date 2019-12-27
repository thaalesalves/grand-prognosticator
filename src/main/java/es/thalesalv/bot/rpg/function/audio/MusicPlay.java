package es.thalesalv.bot.rpg.function.audio;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.function.GenericFunction;
import es.thalesalv.bot.rpg.util.lavaplayer.GuildMusicManager;
import es.thalesalv.bot.rpg.util.lavaplayer.PlayerManager;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
@NoArgsConstructor
@RequiredArgsConstructor
public class MusicPlay implements GenericFunction {

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
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicPlay.class);

    @NonNull
    private GrandPrognosticator grandPrognosticator;

    @Value("${bot.discord.volume}")
    private String botVolume;

    private Boolean isAble() {
        if (!grandPrognosticator.isUserInVoiceChannel(state)) {
            errorMessage = "Pela palavra de Seht, sou compelido. Conecte-se a uma sala de áudio e tente novamente.";
            return false;
        }

        VoiceChannel botChannel = bot.getVoiceState().getChannel();
        String userChannelId = channel.getId();
        if (grandPrognosticator.isConnected(manager) && !userChannelId.equals(botChannel.getId())) {
            errorMessage = "Pela palavra de Seht, já estou conectado em uma sala. Aguarde minha disponibilidade.";
            return false;
        }

        if (!grandPrognosticator.canConnect(channel, bot) || !grandPrognosticator.canSpeak(channel, bot)) {
            errorMessage = "Pela palavra de Seht, não tenho privilégios para acessar a sala requisitada. "
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
            PlayerManager playerManager = PlayerManager.getInstance();
            GuildMusicManager musicManager = playerManager.getGuildMusicManager(guild);
            String songUrl = musicCommands[1];
            UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" },
                    UrlValidator.ALLOW_ALL_SCHEMES);

            if (!urlValidator.isValid(songUrl)) {
                builder.setDescription("Pela palavra de Seht, me foi fornecida uma URL inválida.");
                return builder;
            }

            grandPrognosticator.joinChannel(manager, channel);
            playerManager.loadAndPlay(textChannel, songUrl);
            musicManager.player.setVolume(Integer.parseInt(botVolume));

            builder.setDescription("");
            return builder;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FactotumException(e);
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
