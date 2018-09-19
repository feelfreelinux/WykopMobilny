package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import com.devbrackets.android.exomedia.core.source.MediaSourceProvider
import com.ftinc.kit.util.Utils.getMimeType
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.WykopSchedulers
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed.Coub
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.PhotoViewActions
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_embedview.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import java.io.File
import java.net.URL
import java.nio.charset.Charset
import javax.inject.Inject

class EmbedViewActivity : BaseActivity(), EmbedView {

    companion object {
        const val EXTRA_URL = "EXTRA_URL"
        fun createIntent(context: Context, url: String) =
            Intent(context, EmbedViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
            }
    }

    @Inject lateinit var presenter: EmbedLinkPresenter
    @Inject lateinit var navigatorApi: NewNavigatorApi
    @Inject lateinit var clipboardHelper: ClipboardHelperApi
    @Inject lateinit var settingsPreferencesApi: SettingsPreferencesApi

    override val enableSwipeBackLayout: Boolean = true
    override val isActivityTransfluent: Boolean = true

    private val audioPlayer by lazy {
        ExoPlayerFactory.newSimpleInstance(
            applicationContext,
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())),
            DefaultLoadControl(),
            null,
            DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        )
    }
    private var usingMixedAudio = false
    private val extraUrl by lazy { intent.getStringExtra(EXTRA_URL) }
    lateinit var audioSource: LoopingMediaSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_embedview)
        setSupportActionBar(toolbar)
        toolbar.setBackgroundResource(R.drawable.gradient_toolbar_up)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            presenter.subscribe(this)
            presenter.playUrl(extraUrl)
        }
        videoView.setControls(WykopMediaControls(this))
        videoView.setHandleAudioFocus(false)
        videoView.isFocusable = false
    }

    private fun decodeCoubUrl(input: String): String? {
        val source = StringBuilder(input)
        for (a in 0 until source.length) {
            val c = source[a]
            val lower = Character.toLowerCase(c)
            source.setCharAt(a, if (c == lower) Character.toUpperCase(c) else lower)
        }
        return try {
            String(Base64.decode(source.toString(), Base64.DEFAULT), Charset.forName("UTF-8"))
        } catch (ignore: Exception) {
            null
        }

    }

    override fun checkEmbedSettings() {
        if (settingsPreferencesApi.enableEmbedPlayer) {
            openBrowser(extraUrl)
            finish()
        }
    }

    override fun playUrl(url: URL) {
        prepareVideoView()
        playLoopingSource(Uri.parse(url.toString()))
    }

    override fun playCoub(coub: Coub) {
        val coubUrl = decodeCoubUrl(coub.fileVersions.mobile.gifv)
        presenter.mp4Url = coubUrl!!
        prepareVideoView()
        val videoSource = MediaSourceProvider().generate(this, Handler(Looper.getMainLooper()), Uri.parse(coubUrl!!), null)
        val srcAudio = MediaSourceProvider().generate(this, Handler(Looper.getMainLooper()), Uri.parse(coub.fileVersions.mobile.audio[0]), null)
        audioPlayer.playWhenReady = true
        usingMixedAudio = true
        videoView.setVideoURI(null, LoopingMediaSource(videoSource))
        videoView.setVolume(0f)
        audioSource = LoopingMediaSource(srcAudio)
        audioPlayer.prepare(audioSource, true, true)
    }

    override fun onPause() {
        super.onPause()
        if (usingMixedAudio) {
            audioPlayer.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (usingMixedAudio && ::audioSource.isInitialized) {
            audioPlayer.prepare(audioSource, false, false)
        }
    }

    private fun prepareVideoView() {
        videoView.setOnPreparedListener {
            videoView.isVisible = true
            loadingView.isVisible = false
            videoView.start()
        }
    }

    private fun playLoopingSource(url: Uri) {
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
        if (settingsPreferencesApi.enableYoutubePlayer) {
            navigatorApi.openYoutubeActivity(extraUrl)
        } else {
            openBrowser(extraUrl)
        }
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
            R.id.action_save_mp4 -> if (checkForWriteReadPermission()) saveFile()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareUrl() {
        ShareCompat.IntentBuilder
            .from(this)
            .setType("text/plain")
            .setChooserTitle(R.string.share)
            .setText(extraUrl)
            .startChooser()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (usingMixedAudio) {
            audioPlayer.release()
        }
    }

    private fun saveFile() {
        Single.create<String> {
            val url = presenter.mp4Url
            val path = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                PhotoViewActions.SAVED_FOLDER
            )

            val file = File(path, url.substringAfterLast("/"))
            val request = Request.Builder()
                .url(url)
                .build()
            val result = OkHttpClient().newCall(request).execute()
            if (result.isSuccessful) {
                val sink = Okio.buffer(Okio.sink(file))
                sink.writeAll(result.body()!!.source())
                sink.close()
            } else {
                it.onError(Exception())
            }
            it.onSuccess(path.path)


        }.subscribeOn(WykopSchedulers().backgroundThread())
            .observeOn(WykopSchedulers().mainThread())
            .subscribe({
                val values = ContentValues()

                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                values.put(MediaStore.Images.Media.MIME_TYPE, getMimeType(it))
                values.put(MediaStore.MediaColumns.DATA, it)
                Toast.makeText(this, "Zapisano plik", Toast.LENGTH_SHORT).show()
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }, {
                Toast.makeText(this, "Błąd podczas zapisu pliku", Toast.LENGTH_SHORT).show()
            })
    }

    private fun checkForWriteReadPermission(): Boolean {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1
        )
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return writePermission != PackageManager.PERMISSION_DENIED && readPermission != PackageManager.PERMISSION_DENIED
    }
}