package io.github.feelfreelinux.wykopmobilny.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.projectors.PhotoViewActions
import kotlinx.android.synthetic.main.activity_photoview.*

interface IPhotoViewActivity {
    val url : String
}

fun Activity.launchPhotoView(imageUrl: String) {
    val intent = Intent(this, PhotoViewActivity::class.java)
    intent.putExtra("URL", imageUrl)
    startActivity(intent)
}

class PhotoViewActivity : WykopActivity(), IPhotoViewActivity {
    override val url by lazy {intent.getStringExtra("URL")}
    val photoViewActions by lazy { PhotoViewActions(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoview)
        Picasso.with(this).load(url).into(image)
        setTitle("Mikroblog")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.photoview_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_share ->
                photoViewActions.shareImage()

            R.id.action_save_image ->
                photoViewActions.saveImage()

            R.id.action_copy_url ->
                photoViewActions.copyURL()

            else ->
                return super.onOptionsItemSelected(item)
        }
        return true
    }

}