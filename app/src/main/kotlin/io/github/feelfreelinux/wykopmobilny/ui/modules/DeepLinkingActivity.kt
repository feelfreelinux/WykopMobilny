package io.github.feelfreelinux.wykopmobilny.ui.modules

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler

class DeepLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.dataString!!
        startActivity(WykopLinkHandler.getLinkIntent(url, this))
        finish()
    }

}