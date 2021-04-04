package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.content.Context
import android.content.Intent


class YoutubeActivity {
    companion object {
        fun createIntent(context: Context, url: String): Intent {
            val intent = Intent(context, YTPlayer::class.java)
            intent.putExtra(YTPlayer.EXTRA_VIDEO_ID, YouTubeUrlParser.getVideoId(url))
            YouTubeUrlParser.getTimestamp(url)?.let { t ->
                intent.putExtra(YTPlayer.EXTRA_TIMESTAMP, t)
            }
            return intent
        }
    }
}
