package es.thalesalv.bot.rpg.function.text;

import java.time.LocalDate;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.bean.Watson;
import es.thalesalv.bot.rpg.function.GenericFunction;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@NoArgsConstructor
@RequiredArgsConstructor
public class Clear implements GenericFunction {

    @NonNull
    private Watson watson;

    @NonNull
    private GrandPrognosticator grandPrognosticator;

    private static final Logger LOGGER = LoggerFactory.getLogger(Clear.class);
    private EmbedBuilder builder;
    private MessageHistory history;
    private MessageReceivedEvent event;
    private MessageChannel channel;

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        String arg;
        int argMsgs;

        if ((arg = strings[2]).equals("tudo")) {
            try {
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
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }

            builder.setTitle("Refletindo... calculando... Pela palavra de Seht, eu limpo as mensagens deste canal.");
            builder.setDescription(
                    "Limpas todas as mensagens com até duas semanas de idade deste canal. Esta mensagem será apagada em cinco segundos.");
            Message answer = channel.sendMessage(builder.build()).complete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    answer.delete().complete();
                }
            }, 5000);
            LOGGER.info("Apagando todas as mensagens.");
            return null;
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

                    builder.setTitle("Refletindo... calculando... Pela palavra de Seht, eu limpo as mensagens deste canal.");
                    builder.setDescription("Removidas últimas " + argMsgs + " mensagens. Esta mensagem será apagada em cinco segundos.");
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
                LOGGER.info("Apagando " + argMsgs + " mensagens.");
                return null;
            }
        }

        //Watson.buildSession();
        String watsonReply = watson.sendMessage(strings.toString().replaceAll("\n", "    LINE BREAK    ").trim());
        //Watson.closeSession();
        WatsonMessage watson = new WatsonMessage();
        return watson.execute(watsonReply);
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        builder = grandPrognosticator.buildBuilder(new EmbedBuilder());
        history = new MessageHistory(event.getTextChannel());
        channel = event.getChannel();
        this.event = event;
    }
}
