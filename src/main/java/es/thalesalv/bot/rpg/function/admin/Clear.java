package es.thalesalv.bot.rpg.function.admin;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.function.GenericFunction;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component
@RequiredArgsConstructor
public class Clear implements GenericFunction {

    private final GrandPrognosticator grandPrognosticator;
    private static final Logger LOGGER = LoggerFactory.getLogger(Clear.class);

    private EmbedBuilder builder;
    private MessageHistory history;
    private MessageReceivedEvent event;
    private MessageChannel channel;
    private String[] limpar = { "tudo", "all", "everything" };

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        String arg;
        int argMsgs;

        if (Arrays.asList(limpar).contains((arg = strings[2]))) {

            List<Message> msgs;
            while (true) {
                msgs = history.retrievePast(1).complete();
                Message msg = msgs.get(0);
                LocalDate messageDate = event.getMessage().getCreationTime().toLocalDate();
                LocalDate twoWeeksAgo = messageDate.minusWeeks(2);
                if (msg.getCreationTime().toLocalDate().isBefore(twoWeeksAgo)) {
                    break;
                }
                msg.delete().complete();
            }

            builder.setTitle("Refletindo... calculando... limpando as mensagens deste canal.");
            builder.setDescription("Removidas todas as mensagens "
                    + "com até duas semanas de idade deste canal. Esta mensagem será apagada em cinco segundos.");
            Message answer = channel.sendMessage(builder.build()).complete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    answer.delete().complete();
                }
            }, 5000);
            LOGGER.info("Apagando todas as mensagens.");
        } else if ((argMsgs = Integer.parseInt(arg)) > 0) {
            if (argMsgs <= 100) {
                try {
                    List<Message> msgs = history.retrievePast(argMsgs + 1).complete();
                    for (Message msg : msgs) {
                        LocalDate messageDate = event.getMessage().getCreationTime().toLocalDate();
                        LocalDate twoWeeksAgo = messageDate.minusWeeks(2);
                        if (msg.getCreationTime().toLocalDate().isBefore(twoWeeksAgo)) {
                            break;
                        }
                        msg.delete().complete();
                    }

                    builder.setTitle("Refletindo... calculando... limpando as mensagens deste canal.");
                    builder.setDescription("Removidas últimas " + argMsgs
                            + " mensagens. Esta mensagem será apagada em cinco segundos.");
                    Message answer = channel.sendMessage(builder.build()).complete();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            answer.delete().complete();
                        }
                    }, 5000);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
                LOGGER.info("Apagando {} mensagens.", argMsgs);
            }
        }

        return null;
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        builder = grandPrognosticator.buildBuilder(new EmbedBuilder());
        history = new MessageHistory(event.getTextChannel());
        channel = event.getChannel();
        this.event = event;
    }
}
