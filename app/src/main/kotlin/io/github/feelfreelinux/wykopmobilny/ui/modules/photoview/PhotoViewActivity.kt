package io.github.feelfreelinux.wykopmobilny.ui.modules.photoview

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.indicator.ProgressIndicator
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator
import com.github.piasy.biv.loader.ImageLoader
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.github.piasy.biv.view.BigImageView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.KotlinGlideRequestListener
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.activity_photoview.*
import kotlinx.android.synthetic.main.drawer_header_view_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.lang.Exception
import javax.inject.Inject

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
        image.setImageLoaderCallback(object : ImageLoader.Callback {
            override fun onFinish() {
            }

            override fun onSuccess(image: File?) {
                loadingView.isVisible = false
            }

            override fun onFail(error: Exception?) {
            }

            override fun onCacheHit(image: File?) {
            }

            override fun onCacheMiss(image: File?) {
            }

            override fun onProgress(progress: Int) {
                runOnUiThread {
                    loadingView.progress = progress
                }
            }

            override fun onStart() {
                loadingView.isIndeterminate = false
            }

        })
        image.showImage(Uri.parse(url))
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