package es.thalesalv.bot.rpg.function.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.function.GenericFunction;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

@NoArgsConstructor
@RequiredArgsConstructor
public class ChannelLeave implements GenericFunction {

    private Member member;
    private User caller;
    private Guild guild;
    private GuildVoiceState state;
    private VoiceChannel channel;
    private AudioManager manager;
    private EmbedBuilder builder;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelLeave.class);

    @NonNull
    private GrandPrognosticator grandPrognosticator;

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        try {
            builder.setDescription("Pela palavra de Seht, sou compelido. Saindo de #" + channel.getName());
            grandPrognosticator.leaveChannel(manager);
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
        this.channel = state.getChannel();
        this.manager = guild.getAudioManager();
        builder = grandPrognosticator.buildBuilder(new EmbedBuilder());
        builder.setTitle("Refletindo... processando... iniciando processos sonoros...");
    }
}
