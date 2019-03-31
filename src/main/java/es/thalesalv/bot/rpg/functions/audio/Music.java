package es.thalesalv.bot.rpg.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.util.JBotUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class Music extends Function {

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
    private static final Logger LOGGER = LoggerFactory.getLogger(Music.class);

    public Music(MessageReceivedEvent event) throws Exception {
        this.guild = event.getGuild();
        this.caller = event.getAuthor();
        this.member = guild.getMember(caller);
        this.state = member.getVoiceState();
        this.bot = guild.getSelfMember();
        this.channel = state.getChannel();
        this.manager = guild.getAudioManager();
        this.textChannel = event.getTextChannel();
        builder = JBotUtils.buildBuilder(new EmbedBuilder());
        builder.setTitle("Refletindo... processando... iniciando processos sonoros...");
    }

    private void joinChannel() {
        manager.openAudioConnection(channel);
    }

    private void leaveChannel() {
        manager.closeAudioConnection();
    }

    private Boolean isConnected() {
        return manager.isConnected();
    }

    private Boolean isUserInVoiceChannel() {
        return state.inVoiceChannel();
    }

    private Boolean canConnect() {
        return bot.hasPermission(channel, Permission.VOICE_CONNECT);
    }

    private Boolean canSpeak() {
        return bot.hasPermission(channel, Permission.VOICE_SPEAK);
    }

    private Boolean isAble() {
        if (isConnected()) {
            errorMessage = "Pela palavra de Seht, já estou conectado em uma sala. Aguarde minha disponibilidade.";
            return false;
        }

        if (!canConnect() || !canSpeak()) {
            errorMessage = "Pela palavra de Seht, não tenho privilégios para acessar a sala requisitada. "
                    + "Dirija-se a um Apóstolo para requisitar meu acesso.";
            return false;
        }

        if (!isUserInVoiceChannel()) {
            errorMessage = "Pela palavra de Seht, sou compelido. Conecte-se a uma sala de áudio e tente novamente.";
            return false;
        }

        return true;
    }

    @Override
    public EmbedBuilder buildMessage(String... strings) throws Exception {
        try {
            String command = strings[0];
            builder = JBotUtils.buildBuilder(builder);
            switch (command) {
                case "toque":
                    if (!isAble()) {
                        builder.setDescription(errorMessage);
                        return builder;
                    }

                    joinChannel();
                    break;

                case "conecte":
                    if (!isAble()) {
                        builder.setDescription(errorMessage);
                        return builder;
                    }

                    builder.setDescription("Pela palavra de Seht, sou compelido. Entrando em #" + channel.getName());
                    joinChannel();
                    break;

                case "saia":
                    builder.setDescription("Pela palavra de Seht, sou compelido. Saindo de #" + channel.getName());
                    leaveChannel();
                    break;

                default:
                    builder.setDescription("Pela palavra de Seht, sou compelido. Comando de áudio não reconhecido.");
                    return builder;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        return builder;
    }
}