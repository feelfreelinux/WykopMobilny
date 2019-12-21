package io.github.feelfreelinux.wykopmobilny.utils

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExtensionsTest {

    @Test
    fun `test youtubeTimestampToMsOrNull`() {
        val format1 = "5" // 5 seconds = 5.000ms
        val format2 = "3h3m30s" // 11.010.000ms
        val format3 = "1m10s" // 70.000ms
        val format4 = "25s" // 25.000ms
        val format5 = "malformed_time"

        assertEquals(5_000, format1.youtubeTimestampToMsOrNull())
        assertEquals(11_010_000, format2.youtubeTimestampToMsOrNull())
        assertEquals(70_000, format3.youtubeTimestampToMsOrNull())
        assertEquals(25_000, format4.youtubeTimestampToMsOrNull())
        assertNull(format5.youtubeTimestampToMsOrNull())
    }
}