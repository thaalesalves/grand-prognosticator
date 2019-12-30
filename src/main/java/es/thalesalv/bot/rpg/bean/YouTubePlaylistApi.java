package es.thalesalv.bot.rpg.bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.exception.FactotumException;
import es.thalesalv.bot.rpg.model.YouTubePlaylist;
import es.thalesalv.bot.rpg.model.YouTubeVideo;

@Component
public class YouTubePlaylistApi {

    @Value("${bot.youtube.apikey}")
    private String apiKey;

    private static final String PLAYLIST_API_URL = "https://www.googleapis.com/youtube/v3/playlists?id=PLAYLISTID&key=APIKEY&part=snippet";
    private static final String PLAYLIST_ITEMS_API_URL = "https://www.googleapis.com/youtube/v3/playlistItems?playlistId=PLAYLISTID&key=APIKEY&part=contentDetails";
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubePlaylistApi.class);

    public YouTubePlaylist get(String url) {
        try {
            Gson gson = new Gson();
            String playlistId = url.split("list=")[1];
            String data = lerUrl(PLAYLIST_API_URL.replace("APIKEY", apiKey).replace("PLAYLISTID", playlistId));
            String items = lerUrl(PLAYLIST_ITEMS_API_URL.replace("APIKEY", apiKey).replace("PLAYLISTID", playlistId));

            YouTubePlaylist playlist = new YouTubePlaylist();
            PlaylistData dataResponse = gson.fromJson(items, PlaylistData.class);
            List<YouTubeVideo> videos = new ArrayList<YouTubeVideo>();
            dataResponse.items.forEach(item -> {
                YouTubeVideo video = new YouTubeVideo();
                video.setId(item.contentDetails.videoId);
                video.setUrl("https://www.youtube.com/watch?v=" + item.contentDetails.videoId);
                videos.add(video);
            });

            dataResponse = gson.fromJson(data, PlaylistData.class);
            Items playlistData = dataResponse.items.get(0);
            playlist.setTitle(playlistData.snippet.title);
            playlist.setDescription(playlistData.snippet.description);
            playlist.setId(playlistData.id);
            playlist.setCreator(playlistData.snippet.channelTitle);
            playlist.setUrl("https://www.youtube.com/playlist?list=" + playlistData.id);
            playlist.setVideos(videos);

            return playlist;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FactotumException(e);
        }
    }

    private String lerUrl(String urlString) throws Exception {
        BufferedReader leitor = null;
        try {
            URL url = new URL(urlString);
            leitor = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = leitor.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (leitor != null)
                leitor.close();
        }
    }

    public class PlaylistData {
        public List<Items> items = new ArrayList<Items>();
    }

    public class Items {
        public String id;
        public Snippet snippet;
        public ContentDetails contentDetails;
    }

    public class Snippet {
        public String title;
        public String description;
        public String channelTitle;
    }

    public class ContentDetails {
        public String videoId;
    }
}