package io.github.feelfreelinux.wykopmobilny.ui.modules.photoview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.KotlinGlideRequestListener
import kotlinx.android.synthetic.main.activity_photoview.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import javax.inject.Inject

class PhotoViewActivity : BaseActivity() {

    companion object {
        const val URL_EXTRA = "URL"
        const val SHARE_REQUEST_CODE = 1

        fun createIntent(context: Context, imageUrl: String) =
            Intent(context, PhotoViewActivity::class.java).apply {
                putExtra(PhotoViewActivity.URL_EXTRA, imageUrl)
            }
    }

    @Inject
    lateinit var clipboardHelper: ClipboardHelperApi

    override val enableSwipeBackLayout: Boolean = true // We manually attach it here
    override val isActivityTransfluent: Boolean = true

    val url: String by lazy { intent.getStringExtra(URL_EXTRA)!! }
    private val photoViewActions by lazy { PhotoViewActions(this) as PhotoViewCallbacks }

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
            R.id.action_open_browser -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    fun loadImage() {
        image.isVisible = true
        image.setMinimumDpi(70)
        image.setMinimumTileDpi(240)
        gif.isVisible = false
        GlideApp.with(this).downloadOnly().load(url)
            .into(object : SimpleTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    loadingView.isVisible = false
                    resource.let {
                        image.setImage(io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.ImageSource.uri(resource.absolutePath))
                    }
                }
            })
    }

    private fun loadGif() {
        image.isVisible = false
        gif.isVisible = true
        GlideApp.with(this).load(url)
            .listener(KotlinGlideRequestListener({ loadingView?.isVisible = false }, { loadingView?.isVisible = false }))
            .dontTransform()
            .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
            .into(gif)
    }
}
