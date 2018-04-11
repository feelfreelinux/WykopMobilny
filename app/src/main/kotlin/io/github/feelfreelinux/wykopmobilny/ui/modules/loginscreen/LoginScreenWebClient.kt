package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import android.os.Build
import android.support.annotation.RequiresApi
import android.webkit.*
import android.widget.Toast

typealias TokenUrlCallback = (url : String) -> Unit

class LoginActivityWebClient(private val tokenUrlCallback: TokenUrlCallback) : WebViewClient() {
    @Suppress("OverridingDeprecatedMember")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let { tokenUrlCallback.invoke(it) }
        return super.shouldOverrideUrlLoading(view, url)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.let {
            tokenUrlCallback.invoke(it.url.toString())
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView, url: String?) {
        val cookieManager = CookieManager.getInstance()
        CookieSyncManager.getInstance().sync()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush()
        } else {
        }
    }
}
