package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import org.junit.Test

import org.junit.Assert.*

class YouTubeUrlParserTest {

    @Test
    fun `test getVideoId`() {
        val url1 = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        val url2 = "https://www.youtube.com/watch?v=TeST"

        assertEquals("dQw4w9WgXcQ", YouTubeUrlParser.getVideoId(url1))
        assertNull(YouTubeUrlParser.getVideoId(url2))
    }

    @Test
    fun getTimestamp() {
        val url1 = "https://youtu.be/dQw4w9WgXcQ?t=68"
        val url2 = "https://youtu.be/dQw4w9WgXcQ?t=3m"

        assertEquals("68", YouTubeUrlParser.getTimestamp(url1))
        assertEquals("3m", YouTubeUrlParser.getTimestamp(url2))
    }

    @Test
    fun `test isVideoUrl return false when url is user profile`() {
        val url = "https://www.youtube.com/user/GoogleMobile"
        assertFalse(YouTubeUrlParser.isVideoUrl(url))
    }

    @Test
    fun `test isVideoUrl should return true`() {
        val url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        assertTrue(YouTubeUrlParser.isVideoUrl(url))
    }
}