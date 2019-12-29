package es.thalesalv.bot.rpg.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.thalesalv.bot.rpg.exception.FactotumException;
import lombok.Data;

@Data
public class YouTubeVideo {

    private String id;
    private String title;
    private String creator;
    private String description;
    private String url;
    private String publishedAt;
    private Long likeCount;
    private Long viewCount;
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeVideo.class);

    public LocalDateTime getPublishedAt() {
        return LocalDateTime.parse(this.publishedAt);
    }

    public String parsePublishedAt() throws ParseException {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
            Date date = inputFormat.parse(this.publishedAt);
            return outputFormat.format(date);
        } catch (ParseException e) {
            LOGGER.error("Erro ao parsear data de publicação: {}", e.getMessage());
            throw new FactotumException("Erro ao parsear data de publicação:", e);
        }
    }
}
