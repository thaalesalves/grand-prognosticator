package es.thalesalv.bot.rpg.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
        return new LocalDateTime(this.publishedAt);
    }

    public String parsePublishedAt() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
            Date date = inputFormat.parse(this.publishedAt);
            return outputFormat.format(date);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
