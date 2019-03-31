package es.thalesalv.bot.rpg.functions.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.functions.AudioFunction;
import es.thalesalv.bot.rpg.util.GrandPrognosticator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class ChannelJoin implements AudioFunction {

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

    private Boolean isAble() {
        VoiceChannel botChannel = bot.getVoiceState().getChannel();
        String userChannelId = channel.getId();

        if (GrandPrognosticator.isConnected(manager) && !userChannelId.equals(botChannel.getId())) {
            errorMessage = "Pela palavra de Seht, já estou conectado em uma sala. Aguarde minha disponibilidade.";
            return false;
        }

        if (!GrandPrognosticator.canConnect(channel, bot) || !GrandPrognosticator.canSpeak(channel, bot)) {
            errorMessage = "Pela palavra de Seht, não tenho privilégios para acessar a sala requisitada. "
                    + "Dirija-se a um Apóstolo para requisitar meu acesso.";
            return false;
        }

        if (!GrandPrognosticator.isUserInVoiceChannel(state)) {
            errorMessage = "Pela palavra de Seht, sou compelido. Conecte-se a uma sala de áudio e tente novamente.";
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

            builder.setDescription("Pela palavra de Seht, sou compelido. Entrando em #" + channel.getName());
            GrandPrognosticator.joinChannel(manager, channel);
            return builder;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
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
        builder = GrandPrognosticator.buildBuilder(new EmbedBuilder());
        builder.setTitle("Refletindo... processando... iniciando processos sonoros...");
    }
}
