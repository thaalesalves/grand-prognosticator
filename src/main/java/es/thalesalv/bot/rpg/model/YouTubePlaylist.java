package es.thalesalv.bot.rpg.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class YouTubePlaylist {

    private String id;
    private String title;
    private String description;
    private String creator;
    private String url;
    private List<YouTubeVideo> videos;
}