package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import com.devbrackets.android.exomedia.core.source.MediaSourceProvider
import com.google.android.exoplayer2.source.LoopingMediaSource
import io.github.feelfreelinux.wykopmobilny.BuildConfig
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.activity_embedview.*
import java.net.URL
import javax.inject.Inject


class EmbedViewActivity : BaseActivity(), EmbedView {
    override val enableSwipeBackLayout: Boolean = true
    override val isActivityTransfluent: Boolean = true
    @Inject lateinit var presenter : EmbedLinkPresenter
    @Inject lateinit var navigatorApi : NewNavigatorApi
    val extraUrl by lazy { intent.getStringExtra(EXTRA_URL) }

    companion object {
        val EXTRA_URL = "EXTRA_URL"
        fun createIntent(context : Context, url : String): Intent {
            val intent = Intent(context, EmbedViewActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_embedview)
        if (savedInstanceState == null) {
            printout(extraUrl)
            presenter.subscribe(this)
            presenter.playUrl(extraUrl)
        }
    }

    override fun playUrl(url: URL) {
        prepareVideoView()
        playLoopingSource(Uri.parse(url.toString()))
    }

    fun prepareVideoView() {
        videoView.setControls(WykopMediaControls(this))
        videoView.setHandleAudioFocus(false)
        videoView.setOnPreparedListener {
            videoView.start()
        }
    }

    fun playLoopingSource(url : Uri) {
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            val mediaSource = MediaSourceProvider().generate(this, Handler(), url, null)
            val loopingMediaSource = LoopingMediaSource(mediaSource)
            videoView.setVideoURI(null, loopingMediaSource)
        } else {
            videoView.setOnCompletionListener {
                videoView.restart()
            }
            videoView.setVideoURI(url)
        }
    }

    override fun exitAndOpenYoutubeActivity() {
        navigatorApi.openYoutubeActivity(extraUrl)
        finish()
    }
}