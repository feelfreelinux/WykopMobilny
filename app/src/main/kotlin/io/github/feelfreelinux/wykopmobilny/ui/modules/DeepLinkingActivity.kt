package io.github.feelfreelinux.wykopmobilny.ui.modules

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler

class DeepLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.dataString!!
        val activityToOpen = WykopLinkHandler.getLinkIntent(url, this)
        activityToOpen?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(activityToOpen)
        finish()
    }
}