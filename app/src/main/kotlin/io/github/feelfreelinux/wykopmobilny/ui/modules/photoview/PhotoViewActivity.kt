package io.github.feelfreelinux.wykopmobilny.ui.modules.photoview

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.KotlinGlideRequestListener
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.activity_photoview.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import javax.inject.Inject
import com.davemorrissey.labs.subscaleview.ImageSource
import android.os.Handler
import com.bumptech.glide.request.RequestOptions
import io.github.feelfreelinux.wykopmobilny.base.WykopSchedulers
import io.reactivex.Single
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors


class PhotoViewActivity : BaseActivity() {
    companion object {
        val URL_EXTRA = "URL"
        const val SHARE_REQUEST_CODE = 1

        fun createIntent(context : Context, imageUrl: String): Intent {
            val intent = Intent(context, PhotoViewActivity::class.java)
            intent.putExtra(PhotoViewActivity.URL_EXTRA, imageUrl)
            return intent
        }
    }

    override val enableSwipeBackLayout: Boolean = true
    override val isActivityTransfluent: Boolean = true
    val url: String by lazy { intent.getStringExtra(URL_EXTRA) }
    @Inject lateinit var clipboardHelper : ClipboardHelperApi
    private val photoViewActions by lazy { PhotoViewActions(this, clipboardHelper) as PhotoViewCallbacks }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoview)
        setSupportActionBar(toolbar)
        toolbar.setBackgroundResource(R.drawable.gradient_toolbar_up)
        loadingView.isIndeterminate = true
        title = null
        if (url.endsWith(".gif")) {
            loadGif()
        } else {
            loadImage()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.photoview_menu, menu)

        menu?.findItem(R.id.action_save_mp4)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> photoViewActions.shareImage(url)
            R.id.action_save_image -> photoViewActions.saveImage(url)
            R.id.action_copy_url -> clipboardHelper.copyTextToClipboard(url, "imageUrl")
            R.id.action_open_browser -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SHARE_REQUEST_CODE) {

        }
    }

    fun loadImage() {
        image.isVisible = true
        gif.isVisible = false

        Single.create<File?> {
            val cache = GlideApp.with(this).load(url).downloadOnly(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)

            val file: File?
            file = cache.get()
            it.onSuccess(file)
        }.subscribeOn(WykopSchedulers().backgroundThread())
                .observeOn(WykopSchedulers().mainThread())
                .subscribe({
                    it?.apply {
                        loadingView.isVisible = false
                        image.setImage(ImageSource.uri(it!!.path).tilingEnabled())
                    }
                }, {})

    }

    fun loadGif() {
        image.isVisible = false
        gif.isVisible = true
        GlideApp.with(this).load(url)
                .listener(KotlinGlideRequestListener({ loadingView?.isVisible = false }, { loadingView?.isVisible = false }))
                .dontTransform()
                .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                .into(gif)
    }
}