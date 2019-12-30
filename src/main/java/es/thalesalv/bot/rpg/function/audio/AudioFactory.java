package es.thalesalv.bot.rpg.function.audio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.function.GenericFunction;
import es.thalesalv.bot.rpg.function.audio.actions.ChannelJoin;
import es.thalesalv.bot.rpg.function.audio.actions.ChannelLeave;
import es.thalesalv.bot.rpg.function.audio.actions.MusicPlay;
import es.thalesalv.bot.rpg.function.audio.actions.MusicQueue;
import es.thalesalv.bot.rpg.function.audio.actions.MusicSkip;
import es.thalesalv.bot.rpg.function.audio.actions.MusicStop;
import es.thalesalv.bot.rpg.function.audio.actions.PlaylistPlay;
import es.thalesalv.bot.rpg.function.text.WatsonMessage;
import es.thalesalv.bot.rpg.util.lavaplayer.GuildMusicManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component
@RequiredArgsConstructor
public class AudioFactory implements GenericFunction {

    private MessageReceivedEvent event;
    private final BeanFactory factory;
    private String[] sair = { "saia", "sair", "leave" };
    private String[] parar = { "pare", "para", "parar", "stop" };
    private String[] tocar = { "tocar", "toque", "play" };
    private String[] entrar = { "entre", "entrar", "join" };
    private String[] proximo = { "proximo", "next", "skip", "pular" };
    private String[] fila = { "fila", "lista", "list", "queue", "playlist" };

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        String comando = strings[2];
        GenericFunction function;

        if (Arrays.asList(fila).contains(comando)) {
            if (strings.length > 3)
                function = (PlaylistPlay) factory.getBean("playlistPlay");
            else
                function = (MusicQueue) factory.getBean("musicQueue");
        } else if (Arrays.asList(entrar).contains(comando)) {
            function = (ChannelJoin) factory.getBean("channelJoin");
        } else if (Arrays.asList(tocar).contains(comando)) {
            function = (MusicPlay) factory.getBean("musicPlay");
        } else if (Arrays.asList(proximo).contains(comando)) {
            function = (MusicSkip) factory.getBean("musicSkip");
        } else if (Arrays.asList(parar).contains(comando)) {
            function = (MusicStop) factory.getBean("musicStop");
        } else if (Arrays.asList(sair).contains(comando)) {
            function = (ChannelLeave) factory.getBean("channelLeave");
        } else {
            function = (WatsonMessage) factory.getBean("watsonMessage");
        }

        function.setUp(event);
        return function.execute(strings);
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        this.event = event;
    }

    @Bean
    public Map<Long, GuildMusicManager> musicManagers() {
        return new HashMap<Long, GuildMusicManager>();
    }
}