package io.github.wykopmobilny.ui.modules.embedview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class YouTubeUrlParserTest {

    @Test
    fun `test getVideoId`() {
        val url1 = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        val url2 = "https://www.youtube.com/watch?v=TeST"
        val url3 = "https://consent.youtube.com/m?continue=" +
            "https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DdQw4w9WgXcQ%26feature%3Dyoutu.be&gl=FR&m=0&pc=yt&uxe=23983172&hl=fr&src=1"

        assertEquals("dQw4w9WgXcQ", YouTubeUrlParser.getVideoId(url1))
        assertNull(YouTubeUrlParser.getVideoId(url2))
        assertEquals("dQw4w9WgXcQ", YouTubeUrlParser.getVideoId(url3))
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
        val url1 = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        val url2 = "https://consent.youtube.com/m?continue=" +
            "https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DdQw4w9WgXcQ%26feature%3Dyoutu.be&gl=FR&m=0&pc=yt&uxe=23983172&hl=fr&src=1"
        assertTrue(YouTubeUrlParser.isVideoUrl(url1))
        assertTrue(YouTubeUrlParser.isVideoUrl(url2))
    }
}
