package es.thalesalv.bot.rpg.util;

import java.util.List;
import java.util.logging.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.watson.developer_cloud.assistant.v2.Assistant;
import com.ibm.watson.developer_cloud.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.developer_cloud.assistant.v2.model.DeleteSessionOptions;
import com.ibm.watson.developer_cloud.assistant.v2.model.DialogRuntimeResponseGeneric;
import com.ibm.watson.developer_cloud.assistant.v2.model.MessageInput;
import com.ibm.watson.developer_cloud.assistant.v2.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v2.model.MessageResponse;
import com.ibm.watson.developer_cloud.assistant.v2.model.SessionResponse;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

public class Watson {

    private static final Logger LOGGER = LoggerFactory.getLogger(Watson.class);
    private static Assistant service;
    private static String sessionId;

    public static String sendMessage(String message) throws Exception {
        try {
            MessageInput input = new MessageInput.Builder().text(message).build();
            MessageOptions messageOptions = new MessageOptions.Builder(GrandPrognosticator.WATSON_ASSISTANT_ID, sessionId)
                    .input(input).build();
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
            throw e;
        }

        LOGGER.info("Contexto da mensagem não reconhecido. Nenhum intent relacionado ao conteúdo.");
        return null;
    }

    public static void buildSession() throws Exception {
        try {
            LogManager.getLogManager().reset();
            IamOptions iamOptions = new IamOptions.Builder().apiKey(GrandPrognosticator.WATSON_API_KEY).build();
            service = new Assistant("2018-09-20", iamOptions);
            CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(GrandPrognosticator.WATSON_ASSISTANT_ID)
                    .build();
            SessionResponse session = service.createSession(createSessionOptions).execute();
            sessionId = session.getSessionId();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    public static void closeSession() {
        try {
            DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder(GrandPrognosticator.WATSON_ASSISTANT_ID,
                    sessionId).build();
            service.deleteSession(deleteSessionOptions).execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}