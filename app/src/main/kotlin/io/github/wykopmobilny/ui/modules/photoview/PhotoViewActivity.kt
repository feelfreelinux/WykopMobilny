package io.github.wykopmobilny.ui.modules.photoview

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.databinding.ActivityPhotoviewBinding
import io.github.wykopmobilny.glide.GlideApp
import io.github.wykopmobilny.utils.ClipboardHelperApi
import io.github.wykopmobilny.utils.KotlinGlideRequestListener
import io.github.wykopmobilny.utils.viewBinding
import java.io.File
import javax.inject.Inject

class PhotoViewActivity : BaseActivity() {

    companion object {
        const val URL_EXTRA = "URL"
        const val SHARE_REQUEST_CODE = 1

        fun createIntent(context: Context, imageUrl: String) =
            Intent(context, PhotoViewActivity::class.java).apply {
                putExtra(URL_EXTRA, imageUrl)
            }
    }

    @Inject
    lateinit var clipboardHelper: ClipboardHelperApi

    private val binding by viewBinding(ActivityPhotoviewBinding::inflate)

    override val enableSwipeBackLayout: Boolean = true // We manually attach it here
    override val isActivityTransfluent: Boolean = true

    val url: String by lazy { intent.getStringExtra(URL_EXTRA)!! }
    private val photoViewActions: PhotoViewCallbacks by lazy { PhotoViewActions(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbar.setBackgroundResource(R.drawable.gradient_toolbar_up)
        binding.loadingView.isIndeterminate = true
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
        binding.image.isVisible = true
        binding.image.setMinimumDpi(70)
        binding.image.setMinimumTileDpi(240)
        binding.gif.isVisible = false
        GlideApp.with(this).downloadOnly().load(url)
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    binding.loadingView.isVisible = false
                    binding.image.setImage(ImageSource.uri(resource.absolutePath))
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })
    }

    private fun loadGif() {
        binding.image.isVisible = false
        binding.gif.isVisible = true
        GlideApp.with(this).load(url)
            .listener(KotlinGlideRequestListener({ binding.loadingView.isVisible = false }, { binding.loadingView.isVisible = false }))
            .dontTransform()
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(binding.gif)
    }
}
