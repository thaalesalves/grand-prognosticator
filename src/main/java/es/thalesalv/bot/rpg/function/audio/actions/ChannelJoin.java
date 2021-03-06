package es.thalesalv.bot.rpg.function.audio.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.function.GenericFunction;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

@Component
@RequiredArgsConstructor
public class ChannelJoin implements GenericFunction {

    private Member bot;
    private Member member;
    private User caller;
    private Guild guild;
    private GuildVoiceState state;
    private VoiceChannel channel;
    private AudioManager manager;
    private EmbedBuilder builder;
    private String errorMessage;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelJoin.class);

    private final GrandPrognosticator grandPrognosticator;

    private Boolean isAble() {
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

        if (!grandPrognosticator.isUserInVoiceChannel(state)) {
            errorMessage = "Pela Palavra de Seht, sou compelido. Conecte-se a uma sala de áudio e tente novamente.";
            return false;
        }

        return true;
    }

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        try {
            LOGGER.debug("Entrando em canal -> {}", channel.getName());
            if (!isAble()) {
                builder.setDescription(errorMessage);
                return builder;
            }

            builder.setDescription("Pela Palavra de Seht, sou compelido. Entrando em #" + channel.getName());
            grandPrognosticator.joinChannel(manager, channel);
            return builder;
        } catch (Exception e) {
            throw new FactotumException("Erro ao entrar em canal -> " + channel.getName(), e);
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
        this.manager = guild.getAudioManager();
        builder = grandPrognosticator.buildBuilder(new EmbedBuilder());
        builder.setTitle("Refletindo... processando... iniciando processos sonoros...");
    }
}
