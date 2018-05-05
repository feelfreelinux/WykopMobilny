package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import io.github.feelfreelinux.wykopmobilny.GOOGLE_KEY
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import kotlinx.android.synthetic.main.activity_youtubeplayer.*
import java.util.regex.Pattern

class YoutubeActivity : BaseActivity(), YouTubePlayer.OnInitializedListener {
    override val enableSwipeBackLayout = true
    override val isActivityTransfluent = true

    val extraURL by lazy { intent.getStringExtra(EXTRA_URL) }

    companion object {
        val EXTRA_URL = "URLEXTRA"
        fun createIntent(context: Context, url: String): Intent {
            val intent = Intent(context, YoutubeActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            return intent
        }
    }

    val youtubePlayer by lazy { YouTubePlayerSupportFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtubeplayer)

        supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.contentView, youtubePlayer).commit()
        }

        youtubePlayer.retainInstance = true
        youtubePlayer.initialize(GOOGLE_KEY, this)
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, result: YouTubeInitializationResult?) {
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, restored: Boolean) {
        if (!restored) {
            player?.prepare()
            player?.cueVideo(extractVideoIdFromUrl(extraURL.replace("m.", "")))
        }
    }

    fun YouTubePlayer.prepare() {
        fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION or YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE or YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
        setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)

    }

    val youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"
    val videoIdRegex = arrayOf("\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9_\\-]*)")

    fun extractVideoIdFromUrl(url: String): String? {
        val youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url)

        for (regex in videoIdRegex) {
            val compiledPattern = Pattern.compile(regex)
            val matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain)

            if (matcher.find()) {
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