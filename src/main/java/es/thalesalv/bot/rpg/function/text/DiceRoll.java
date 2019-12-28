package es.thalesalv.bot.rpg.function.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.function.GenericFunction;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component
@RequiredArgsConstructor
public class DiceRoll implements GenericFunction {

    private final GrandPrognosticator grandPrognosticator;
    private static final Logger LOGGER = LoggerFactory.getLogger(DiceRoll.class);

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        try {
            String authorMention = strings[0];
            String authorName = strings[1];
            String[] dicesToRoll = strings[4].split("d");

            Integer diceQty = dicesToRoll[0].equals("") ? 1 : Integer.parseInt(dicesToRoll[0]);
            Integer dice = Integer.parseInt(dicesToRoll[1]);

            LOGGER.info("Rolando " + diceQty + "d" + dice + " para " + authorName);

            List<String> diceList = new ArrayList<String>();
            for (int i = 0; i < diceQty; i++) {
                int diceRoll = new Random().nextInt(dice) + 1;
                diceList.add(String.valueOf(diceRoll));
            }

            List<Integer> diceListInteger = diceList.stream().map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());
            String diceRolls = String.join(", ", diceList);

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Refletindo... calculando... Pela Palavra de Seht, eu rolo " + diceQty
                    + (diceQty > 1 ? " dados" : " dado") + " de " + dice + " lados.");
            builder.setDescription(authorMention + ", "
                    + (diceQty > 1
                            ? "seus dados são: " + diceRolls + " ("
                                    + diceListInteger.stream().mapToInt(Integer::intValue).sum() + ")"
                            : "seu dado é: " + diceRolls));
            return grandPrognosticator.buildBuilder(builder);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FactotumException(e);
        }
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
    }
}
