package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import com.devbrackets.android.exomedia.core.source.MediaSourceProvider
import com.devbrackets.android.exomedia.ui.widget.VideoControlsMobile
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView
import io.github.feelfreelinux.wykopmobilny.BuildConfig
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.utils.FullScreenManager
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.activity_embedview.*
import kotlinx.android.synthetic.main.activity_youtubeplayer.*
import java.util.regex.Pattern
import javax.inject.Inject


class YoutubeViewActivity : BaseActivity() {
    override val enableSwipeBackLayout: Boolean = true
    override val isActivityTransfluent: Boolean = true
    val extraUrl by lazy { intent.getStringExtra(EXTRA_URL) }
    val fullscreenManager by lazy { FullScreenManager(this) }

    companion object {
        val EXTRA_URL = "EXTRA_URL"
        fun createIntent(context : Context, url : String): Intent {
            val intent = Intent(context, YoutubeViewActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtubeplayer)
        lifecycle.addObserver(youtubePlayerView)
        if (savedInstanceState == null) {
            youtubePlayerView.initialize({
                it.addListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady() {
                        extractVideoIdFromUrl(extraUrl)?.apply {
                            it.loadVideo(this, 0f)
                        }
                    }
                })
                youtubePlayerView.addFullScreenListener( object : YouTubePlayerFullScreenListener {
                    override fun onYouTubePlayerExitFullScreen() {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        fullscreenManager.exitFullScreen()
                    }

                    override fun onYouTubePlayerEnterFullScreen() {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        fullscreenManager.enterFullScreen()
                    }
                })
            }, true)
        }

    }

    val youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"
    val videoIdRegex = arrayOf("\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9_\\-]*)")

    fun extractVideoIdFromUrl(url: String): String? {
        val youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url)

        for (regex in videoIdRegex) {
            val compiledPattern = Pattern.compile(regex)
            val matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain)

            if (matcher.find()) {
                printout("GOTITTT" + matcher.group(1))
                return matcher.group(1)
            }
        }

        return null
    }

    private fun youTubeLinkWithoutProtocolAndDomain(url: String): String {
        val compiledPattern = Pattern.compile(youTubeUrlRegEx)
        val matcher = compiledPattern.matcher(url)

        return if (matcher.find()) {
            url.replace(matcher.group(), "")
        } else url
    }

}