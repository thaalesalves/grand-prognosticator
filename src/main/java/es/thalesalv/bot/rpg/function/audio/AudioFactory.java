package es.thalesalv.bot.rpg.function.audio;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.function.GenericFunction;
import es.thalesalv.bot.rpg.function.audio.actions.ChannelJoin;
import es.thalesalv.bot.rpg.function.audio.actions.ChannelLeave;
import es.thalesalv.bot.rpg.function.audio.actions.MusicPlay;
import es.thalesalv.bot.rpg.function.audio.actions.MusicQueue;
import es.thalesalv.bot.rpg.function.audio.actions.MusicSkip;
import es.thalesalv.bot.rpg.function.audio.actions.MusicStop;
import es.thalesalv.bot.rpg.function.text.WatsonMessage;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component
@RequiredArgsConstructor
public class AudioFactory implements GenericFunction {

    private MessageReceivedEvent event;
    private final BeanFactory factory;

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        String comando = strings[2];
        GenericFunction function;

        if ("entre".equals(comando) || "join".equals(comando)) {
            function = (ChannelJoin) factory.getBean("channelJoin");
        } else if ("toque".equals(comando) || "play".equals(comando)) {
            function = (MusicPlay) factory.getBean("musicPlay");
        } else if ("proximo".equals(comando) || "next".equals(comando) || "skip".equals(comando)) {
            function = (MusicSkip) factory.getBean("musicSkip");
        } else if ("pare".equals(comando) || "stop".equals(comando)) {
            function = (MusicStop) factory.getBean("musicStop");
        } else if ("lista".equals(comando) || "list".equals(comando) || "queue".equals(comando)) {
            function = (MusicQueue) factory.getBean("musicQueue");
        } else if ("saia".equals(comando) || "leave".equals(comando)) {
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
}