package es.thalesalv.bot.rpg.function.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.bean.GrandPrognosticator;
import es.thalesalv.bot.rpg.function.GenericFunction;
import es.thalesalv.bot.rpg.util.lavaplayer.GuildMusicManager;
import es.thalesalv.bot.rpg.util.lavaplayer.PlayerManager;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@NoArgsConstructor
@RequiredArgsConstructor
public class MusicStop implements GenericFunction {

    private Guild guild;
    private EmbedBuilder builder;
    private PlayerManager playerManager;
    private GuildMusicManager musicManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicStop.class);

    @NonNull
    private GrandPrognosticator grandPrognosticator;

    @Override
    public EmbedBuilder execute(String... strings) throws Exception {
        try {
            musicManager.scheduler.clearQueue();
            musicManager.player.stopTrack();
            musicManager.player.setPaused(false);
            builder.setDescription("Pela palavra de Seht, sou compelido. Cancelando execução musical.");
            return builder;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setUp(MessageReceivedEvent event) throws Exception {
        this.guild = event.getGuild();
        this.playerManager = PlayerManager.getInstance();
        this.musicManager = playerManager.getGuildMusicManager(guild);
        builder = grandPrognosticator.buildBuilder(new EmbedBuilder());
        builder.setTitle("Refletindo... processando... iniciando processos sonoros...");
    }
}
