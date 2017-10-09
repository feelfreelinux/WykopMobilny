package io.github.feelfreelinux.wykopmobilny.ui.modules.photoview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import kotlinx.android.synthetic.main.activity_photoview.*
import kotlinx.android.synthetic.main.toolbar.*

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
    private val photoViewActions by lazy { PhotoViewActions(this) as PhotoViewCallbacks }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoview)
        setSupportActionBar(toolbar)
        image.loadImage(url)
        title = null
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
            R.id.action_copy_url -> photoViewActions.copyURL()
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}