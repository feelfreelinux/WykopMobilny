package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import io.github.feelfreelinux.wykopmobilny.ui.modules.IntentCreationFailedException

import android.content.Context
import android.content.Intent


class YoutubeActivity {
    companion object {
        fun createIntent(context: Context, url: String): Intent {
            val videoId = YouTubeUrlParser.getVideoId(url)
                    ?: throw IntentCreationFailedException("Could not parse YouTube url: '$url'")

            val intent = Intent(context, YTPlayer::class.java)
            intent.putExtra(YTPlayer.EXTRA_VIDEO_ID, videoId)
            YouTubeUrlParser.getTimestamp(url)?.let { t ->
                intent.putExtra(YTPlayer.EXTRA_TIMESTAMP, t)
            }
            return intent
        }
    }
}
