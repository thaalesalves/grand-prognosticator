package es.thalesalv.bot.rpg.bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.thalesalv.bot.rpg.model.YouTubeVideo;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class YouTube {

    private static String API_KEY;
    private static String API_URL;
    private static String[] PART = { "snippet", "contentDetails", "statistics", "status" };
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTube.class);

    public YouTubeVideo get(String url) {

        Gson gson = new Gson();
        String data = null;
        try {
            String videoId = url.split("v=")[1];
            String part = String.join(",", PART);
            data = lerUrl(API_URL.replace("APIKEY", API_KEY).replace("VIDEOID", videoId).replace("PART", part));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }

        RespostaYouTube response = gson.fromJson(data, RespostaYouTube.class);
        Items videoData = response.items.get(0);

        YouTubeVideo video = new YouTubeVideo();
        video.setTitle(videoData.snippet.title);
        video.setDescription(videoData.snippet.description);
        video.setId(videoData.id);
        video.setLikeCount(videoData.statistics.likeCount);
        video.setViewCount(videoData.statistics.viewCount);
        video.setUrl(url);
        video.setCreator(videoData.snippet.channelTitle);
        video.setPublishedAt(videoData.snippet.publishedAt);

        return video;
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

    public class RespostaYouTube {
        public List<Items> items = new ArrayList<Items>();
    }

    public class Items {
        public Snippet snippet;
        public Statistics statistics;
        public String id;
    }

    public class Snippet {
        public String title;
        public String description;
        public String publishedAt;
        public String channelTitle;
        public List<String> tags = new ArrayList<String>();
    }

    public class Statistics {
        public long likeCount;
        public long viewCount;
    }
}
