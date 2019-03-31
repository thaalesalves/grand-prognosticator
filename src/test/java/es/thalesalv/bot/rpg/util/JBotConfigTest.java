package es.thalesalv.bot.rpg.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JBotConfigTest {

	@Test
	public void testDiscordBot() throws Exception {
		assertEquals("363936626201853954", GrandPrognosticator.BOT_TOKEN);
	}
}
