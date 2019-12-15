package es.thalesalv.bot.rpg.functions.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.functions.GenericFunction;
import es.thalesalv.bot.rpg.util.GrandPrognosticator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DiceRoll implements GenericFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiceRoll.class);

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        try {
            Integer diceQty = strings[0].equals("") ? 1 : Integer.parseInt(strings[0]);
            Integer dice = Integer.parseInt(strings[1]);
            String authorMention = strings[2];
            String author = strings[3];

            LOGGER.info("Rolando " + diceQty + "d" + dice + " para " + author);

            List<String> diceList = new ArrayList<String>();
            for (int i = 0; i < diceQty; i++) {
                int diceRoll = new Random().nextInt(dice) + 1;
                diceList.add(String.valueOf(diceRoll));
            }

            List<Integer> diceListInteger = diceList.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
            String diceRolls = String.join(", ", diceList);

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Refletindo... calculando... Pela palavra de Seht, eu rolo " + diceQty
                    + (diceQty > 1 ? " dados" : " dado") + " de " + dice + " lados.");
            builder.setDescription(authorMention + ", "
                    + (diceQty > 1
                            ? "seus dados são: " + diceRolls + " (" + diceListInteger.stream().mapToInt(Integer::intValue).sum()
                                    + ")"
                            : "seu dado é: " + diceRolls));
            return GrandPrognosticator.buildBuilder(builder);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        // TODO Auto-generated method stub

    }
}
