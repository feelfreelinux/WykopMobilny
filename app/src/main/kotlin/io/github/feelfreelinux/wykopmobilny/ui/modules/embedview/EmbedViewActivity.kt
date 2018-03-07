package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ShareCompat
import android.view.Menu
import android.view.MenuItem
import com.devbrackets.android.exomedia.core.source.MediaSourceProvider
import com.google.android.exoplayer2.source.LoopingMediaSource
import io.github.feelfreelinux.wykopmobilny.BuildConfig
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.activity_embedview.*
import kotlinx.android.synthetic.main.toolbar.*
import java.net.URL
import javax.inject.Inject


class EmbedViewActivity : BaseActivity(), EmbedView {
    override val enableSwipeBackLayout: Boolean = true
    override val isActivityTransfluent: Boolean = true
    @Inject lateinit var presenter : EmbedLinkPresenter
    @Inject lateinit var navigatorApi : NewNavigatorApi
    @Inject lateinit var clipboardHelper : ClipboardHelperApi
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
        setSupportActionBar(toolbar)
        toolbar.setBackgroundResource(R.drawable.gradient_toolbar_up)
        supportActionBar?.title = null

        if (savedInstanceState == null) {
            presenter.subscribe(this)
            presenter.playUrl(extraUrl)
        }
        videoView.setControls(WykopMediaControls(this))
        videoView.setHandleAudioFocus(false)
        videoView.isFocusable = false
    }

    override fun playUrl(url: URL) {
        prepareVideoView()
        playLoopingSource(Uri.parse(url.toString()))
    }

    fun prepareVideoView() {
        videoView.setOnPreparedListener {
            videoView.isVisible = true
            loadingView.isVisible = false
            videoView.start()
        }
    }

    fun playLoopingSource(url : Uri) {
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            val mediaSource = MediaSourceProvider().generate(this, Handler(Looper.getMainLooper()), url, null)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.photoview_menu, menu)
        menu?.findItem(R.id.action_save_image)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_copy_url -> clipboardHelper.copyTextToClipboard(extraUrl, "imageUrl")
            R.id.action_open_browser -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(extraUrl)
                startActivity(i)
            }
            R.id.action_share -> {
                shareUrl()
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun shareUrl() {
        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(extraUrl)
                .startChooser()
    }
}