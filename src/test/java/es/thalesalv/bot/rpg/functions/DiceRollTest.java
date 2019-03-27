package es.thalesalv.bot.rpg.functions;

import java.util.List;

import org.junit.Test;

public class DiceRollTest {

	@Test
	public void testRollDice() throws Exception {
	    String cmd = "10d20";
	    String[] dicesToRoll = cmd.split("d");
        int dice = Integer.parseInt(dicesToRoll[1]);
        int diceQty = Integer.parseInt(dicesToRoll[0]);
        String diceRolls = String.join(", ", DiceRoll.rollDice(diceQty, dice));
        System.out.println(diceRolls);
        
        cmd = "25d100";
        dicesToRoll = cmd.split("d");
        dice = Integer.parseInt(dicesToRoll[1]);
        diceQty = Integer.parseInt(dicesToRoll[0]);
        diceRolls = String.join(", ", DiceRoll.rollDice(diceQty, dice));
        System.out.println(diceRolls);
	}
}
