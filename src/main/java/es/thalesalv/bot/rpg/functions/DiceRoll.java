package es.thalesalv.bot.rpg.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiceRoll {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiceRoll.class.getName());

    public static List<String> rollDice(Integer numberOfDices, Integer dice) throws Exception {
        List<String> rollResults = new ArrayList<String>();

        try {
            for (int i = 0; i < numberOfDices; i++) {
                int diceRoll = new Random().nextInt(dice) + 1;
                rollResults.add(String.valueOf(diceRoll));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        return rollResults;
    }
}
