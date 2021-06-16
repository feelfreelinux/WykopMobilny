package io.github.wykopmobilny.ui.modules.embedview

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.base.WykopSchedulers
import io.github.wykopmobilny.databinding.ActivityEmbedviewBinding
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.ui.modules.photoview.PhotoViewActions
import io.github.wykopmobilny.utils.ClipboardHelperApi
import io.github.wykopmobilny.utils.openBrowser
import io.github.wykopmobilny.utils.viewBinding
import io.github.wykopmobilny.utils.wykopLog
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.File
import javax.inject.Inject

class EmbedViewActivity : BaseActivity(), EmbedView {

    companion object {
        const val EXTRA_URL = "EXTRA_URL"
        fun createIntent(context: Context, url: String) =
            Intent(context, EmbedViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
            }
    }

    @Inject
    lateinit var presenter: EmbedLinkPresenter

    @Inject
    lateinit var navigatorApi: NewNavigatorApi

    @Inject
    lateinit var clipboardHelper: ClipboardHelperApi

    @Inject
    lateinit var settingsPreferencesApi: SettingsPreferencesApi

    private val binding by viewBinding(ActivityEmbedviewBinding::inflate)

    override val enableSwipeBackLayout: Boolean = true
    override val isActivityTransfluent: Boolean = true

    private val extraUrl by lazy { intent.getStringExtra(EXTRA_URL)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbar.setBackgroundResource(R.drawable.gradient_toolbar_up)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            presenter.subscribe(this)
            presenter.playUrl(extraUrl)
        }
        binding.videoView.player = SimpleExoPlayer.Builder(this).build()
        binding.videoView.controllerAutoShow = false
        binding.videoView.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING)
    }

    override fun onPause() {
        super.onPause()
        binding.videoView.player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.player?.release()
    }

    override fun checkEmbedSettings() {
        if (settingsPreferencesApi.enableEmbedPlayer) {
            openBrowser(extraUrl)
            finish()
        }
    }

    override fun playUrl(url: String) {
        val mediaItem: MediaItem = MediaItem.fromUri(url)

        binding.videoView.player?.apply {
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
            play()
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
            R.id.action_open_browser -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(extraUrl)))
            R.id.action_share -> shareUrl()
            R.id.action_save_mp4 ->
                if (checkForWriteReadPermission()) {
                    saveFile()
                }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareUrl() {
        ShareCompat.IntentBuilder(this)
            .setType("text/plain")
            .setChooserTitle(R.string.share)
            .setText(extraUrl)
            .startChooser()
    }

    private fun getFilenameFromResult(response: okhttp3.Response): String {
        val contentDispositionRegex =
            "(?:inline|attachment)(?:;\\s*)filename\\s*=\\s*[\"'](.*)[\"']".toRegex()
        val cd = response.header("content-disposition")
        if (cd != null) {
            val match = contentDispositionRegex.find(cd)
            if (match != null) {
                return match.groupValues[1]
            }
        }

        val pathSegments = response.request.url.pathSegments
        return pathSegments.last()
    }

    private fun saveFile() {
        Single.create<String> {
            val url = presenter.mp4Url
            val path = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                PhotoViewActions.SAVED_FOLDER,
            )
            if (!path.exists()) {
                path.mkdirs()
            }

            val request = Request.Builder()
                .url(url)
                .build()
            val result = OkHttpClient().newCall(request).execute()
            if (result.isSuccessful) {
                val file = File(path, getFilenameFromResult(result))
                val sink = file.sink().buffer()
                sink.writeAll(result.body!!.source())
                sink.close()
            } else {
                it.onError(Exception("Could not download the file, http code ${result.code}"))
            }
            it.onSuccess(path.path)
        }.subscribeOn(WykopSchedulers().backgroundThread())
            .observeOn(WykopSchedulers().mainThread())
            .subscribe(
                {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                    values.put(MediaStore.Images.Media.MIME_TYPE, getMimeType(it))
                    values.put(MediaStore.MediaColumns.DATA, it)
                    Toast.makeText(this, R.string.save_file_ok, Toast.LENGTH_SHORT).show()
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                },
                {
                    wykopLog(Log::e, "Exception when trying to save file", it)
                    Toast.makeText(this, R.string.save_file_failed, Toast.LENGTH_SHORT).show()
                },
            )
    }

    private fun checkForWriteReadPermission(): Boolean {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1,
        )
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
        return writePermission != PackageManager.PERMISSION_DENIED && readPermission != PackageManager.PERMISSION_DENIED
    }

    private fun getMimeType(url: String): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        return if (extension != null) {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        } else {
            null
        }
    }
}
