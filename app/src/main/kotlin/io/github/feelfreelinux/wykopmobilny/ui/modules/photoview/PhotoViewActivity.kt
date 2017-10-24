package io.github.feelfreelinux.wykopmobilny.ui.modules.photoview

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import kotlinx.android.synthetic.main.activity_photoview.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

fun Context.launchPhotoView(imageUrl: String) {
    val intent = Intent(this, PhotoViewActivity::class.java)
    intent.putExtra(PhotoViewActivity.URL_EXTRA, imageUrl)
    startActivity(intent)
}

class PhotoViewActivity : BaseActivity() {
    companion object {
        val URL_EXTRA = "URL"
    }
    val url: String by lazy { intent.getStringExtra(URL_EXTRA) }
    @Inject lateinit var clipboardHelper : ClipboardHelperApi
    private val photoViewActions by lazy { PhotoViewActions(this, clipboardHelper) as PhotoViewCallbacks }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoview)
        setSupportActionBar(toolbar)
        WykopApp.uiInjector.inject(this)
        title = null
        image.loadImage(url)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.photoview_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> photoViewActions.shareImage()
            R.id.action_save_image -> photoViewActions.saveImage()
            R.id.action_copy_url -> clipboardHelper.copyTextToClipboard(url, "imageUrl")
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}