package es.thalesalv.bot.rpg.service;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.thalesalv.bot.rpg.bean.Watson;
import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.function.GenericFunction;
import es.thalesalv.bot.rpg.function.text.WatsonMessage;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Service
@RequiredArgsConstructor
public class ChatService extends ListenerAdapter {

    private Guild guild;
    private static final String CHAT_LOGGER = "[{}] {} disse em {}: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private final BeanFactory factory;
    private final Watson watson;

    @Value("${bot.discord.api.id}")
    private String botId;

    @Value("${bot.discord.message.operator}")
    private String botOperator;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            guild = event.getGuild();
            MessageChannel channel = event.getChannel();
            User author = event.getAuthor();
            String rawContent = event.getMessage().getContentRaw();
            EmbedBuilder builder = new EmbedBuilder();

            String[] commands = rawContent.split(" "); //rawContent.replaceAll("\\p{Punct}", "").split(" ");
            User bot = guild.getMemberById(botId).getUser();
            Boolean isElegible = event.isFromType(ChannelType.TEXT)
                    && (event.getMessage().getMentionedUsers().contains(bot) || rawContent.split(" ")[0].equals(botOperator));

            if (!author.isBot() && isElegible) {
                LOGGER.debug(CHAT_LOGGER, guild.getName(), author.getName(), channel.getName(), rawContent);
                String watsonReply = watson.sendMessage(rawContent.replaceAll("\n", "    LINE BREAK    ").trim());
                GenericFunction function;
                try {
                    LOGGER.debug("Função {} sendo chamada por {}", watsonReply, author.getName());
                    function = (GenericFunction) factory.getBean(watsonReply);
                    function.setUp(event);
                    builder = function.execute(commands);
                } catch (BeansException e) {
                    LOGGER.debug("Não existe bean para o comando pedido: " + rawContent, e);
                    function = (WatsonMessage) factory.getBean("watsonMessage");
                    builder = function.execute(watsonReply);
                }
                
                if (builder != null)
                    channel.sendMessage(builder.build()).complete();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FactotumException("Erro no serviço de chat.", e);
        }
    }

    @PreDestroy
    private void die() {
        LOGGER.debug("Matando bean de serviço de chat.");
        guild.getJDA().shutdown();
    }
}
