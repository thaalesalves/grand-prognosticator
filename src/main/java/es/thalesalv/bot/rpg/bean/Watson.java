package es.thalesalv.bot.rpg.bean;

import java.util.List;
import java.util.logging.LogManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.ibm.watson.developer_cloud.assistant.v2.Assistant;
import com.ibm.watson.developer_cloud.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.developer_cloud.assistant.v2.model.DeleteSessionOptions;
import com.ibm.watson.developer_cloud.assistant.v2.model.DialogRuntimeResponseGeneric;
import com.ibm.watson.developer_cloud.assistant.v2.model.MessageInput;
import com.ibm.watson.developer_cloud.assistant.v2.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v2.model.MessageResponse;
import com.ibm.watson.developer_cloud.assistant.v2.model.SessionResponse;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.exception.WatsonException;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class Watson {

    private static String API_KEY;
    private static String ASSISTANT_ID;
    private static Assistant service;
    private static String sessionId;
    private static final Logger LOGGER = LoggerFactory.getLogger(Watson.class);

    @Value("${bot.watson.apikey}")
    private void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }

    @Value("${bot.watson.assistantid}")
    private void setAssistantId(String assistantId) {
        ASSISTANT_ID = assistantId;
    }

    public String sendMessage(String message) throws Exception {
        try {
            MessageInput input = new MessageInput.Builder().text(message).build();
            MessageOptions messageOptions = new MessageOptions.Builder(ASSISTANT_ID, sessionId).input(input).build();
            MessageResponse response = service.message(messageOptions).execute();
            LOGGER.info("Mensagem recebida pelo Watson. Verificando contexto.");

            DialogRuntimeResponseGeneric watsonResponse;
            List<DialogRuntimeResponseGeneric> responseGeneric = response.getOutput().getGeneric();
            if ((watsonResponse = responseGeneric.get(0)) != null) {
                LOGGER.info("Contexto reconhecido. Enviando resposta do Watson.");
                return watsonResponse.getText();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new WatsonException("Erro no envio de mensagens para o Watson", e);
        }

        LOGGER.info("Contexto da mensagem não reconhecido. Nenhum intent relacionado ao conteúdo.");
        return null;
    }

    @PostConstruct
    private void init() throws Exception {
        try {
            LogManager.getLogManager().reset();
            IamOptions iamOptions = new IamOptions.Builder().apiKey(API_KEY).build();
            service = new Assistant("2018-09-20", iamOptions);
            CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(ASSISTANT_ID).build();
            SessionResponse session = service.createSession(createSessionOptions).execute();
            sessionId = session.getSessionId();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new WatsonException("Erro ao criar sessão do Watson", e);
        }
    }

    @PreDestroy
    private void die() throws Exception {
        try {
            DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder(ASSISTANT_ID, sessionId)
                    .build();
            service.deleteSession(deleteSessionOptions).execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new WatsonException("Erro ao fechar sessão do Watson", e);
        }
    }
}