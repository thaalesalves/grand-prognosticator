package es.thalesalv.bot.rpg.functions.sheet;

import java.util.Random;

import es.thalesalv.bot.rpg.functions.GenericFunction;
import es.thalesalv.bot.rpg.model.sheet.Attributes;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RandomAmaranthSheet implements GenericFunction {

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        // TODO Auto-generated method stub

    }

    private void randomizeSheet() throws Exception {
        Random x = new Random();
        int nextInt = x.nextInt(2);
        Attributes[] values = Attributes.values();

        Attributes primary = values[nextInt];

    }
}
