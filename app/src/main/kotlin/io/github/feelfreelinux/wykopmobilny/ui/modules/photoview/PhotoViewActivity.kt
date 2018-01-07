package io.github.feelfreelinux.wykopmobilny.ui.modules.photoview

import android.content.Context
import android.content.Intent
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
import javax.inject.Inject

class PhotoViewActivity : BaseActivity() {
    companion object {
        val URL_EXTRA = "URL"

        fun createIntent(context : Context, imageUrl: String): Intent {
            val intent = Intent(context, PhotoViewActivity::class.java)
            intent.putExtra(PhotoViewActivity.URL_EXTRA, imageUrl)
            return intent
        }
    }
    val url: String by lazy { intent.getStringExtra(URL_EXTRA) }
    @Inject lateinit var clipboardHelper : ClipboardHelperApi
    private val photoViewActions by lazy { PhotoViewActions(this, clipboardHelper) as PhotoViewCallbacks }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoview)
        setSupportActionBar(toolbar)
        title = null
        GlideApp.with(this).load(url)
                .listener(KotlinGlideRequestListener({ loadingView.isVisible = false }, { loadingView.isVisible = false }))
                .into(image)
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