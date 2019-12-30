package es.thalesalv.bot.rpg.bean;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.thalesalv.bot.rpg.model.YouTubePlaylist;
import es.thalesalv.bot.rpg.model.YouTubeVideo;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = "bot.youtube.apikey:API_KEY")
@ContextConfiguration(classes = YouTubePlaylistApi.class)
public class YouTubePlaylistApiTest {

    @Autowired
    private YouTubePlaylistApi playlistApi;

    @Test
    public void testPlaylist() {
        String playlistUrl = "https://www.youtube.com/playlist?list=PLQv7aTpyBPhmIE7rsLstkpa3BfNg-1WOd";
        YouTubePlaylist playlist = playlistApi.get(playlistUrl);
        YouTubeVideo video = playlist.getVideos().get(0);

        Assert.assertEquals("Underground Indie", playlist.getTitle());
        Assert.assertEquals("Grace Norris", playlist.getCreator());
        Assert.assertEquals("PLQv7aTpyBPhmIE7rsLstkpa3BfNg-1WOd", playlist.getId());
        Assert.assertEquals(StringUtils.EMPTY, playlist.getDescription());
        Assert.assertEquals("M3IWsWzSo14", video.getId());
        Assert.assertEquals("https://www.youtube.com/watch?v=M3IWsWzSo14", video.getUrl());
    }
}